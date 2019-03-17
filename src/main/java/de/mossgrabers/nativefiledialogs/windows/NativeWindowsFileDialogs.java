// Written by Jürgen Moßgraber - mossgrabers.de
// (c) 2019
// Licensed under LGPLv3 - http://www.gnu.org/licenses/lgpl-3.0.txt

package de.mossgrabers.nativefiledialogs.windows;

import de.mossgrabers.nativefiledialogs.AbstractNativeFileDialogs;
import de.mossgrabers.nativefiledialogs.FileFilter;
import de.mossgrabers.nativefiledialogs.windows.Shell32.BrowseInfoCallback;

import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.WString;
import com.sun.jna.platform.win32.Ole32;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.platform.win32.WinDef.LPARAM;
import com.sun.jna.platform.win32.WinDef.WPARAM;

import java.io.File;
import java.io.IOException;


/**
 * The Windows implementation for the file dialogs.
 *
 * @author J&uuml;rgen Mo&szlig;graber
 */
public class NativeWindowsFileDialogs extends AbstractNativeFileDialogs
{
    private static final int MAX_PATH_LENGTH = 4000;

    private final String     parentWindowClassName;


    /**
     * Creates a new file dialog instance with the initial directory.
     *
     * @param currentDirectory The initial directory, may be null
     * @param parentWindowClassName The name of the window class name of the parent window
     */
    public NativeWindowsFileDialogs (final File currentDirectory, final String parentWindowClassName)
    {
        super (currentDirectory);
        this.parentWindowClassName = parentWindowClassName;
    }


    /** {@inheritDoc} */
    @Override
    public File selectFile (final String title, final FileFilter... filters) throws IOException
    {
        return this.showDialog (true, title, filters);
    }


    /** {@inheritDoc} */
    @Override
    public File selectNewFile (final String title, final FileFilter... filters) throws IOException
    {
        return this.showDialog (false, title, filters);
    }


    /** {@inheritDoc} */
    @Override
    public File selectFolder (final String title)
    {
        // Listens for callback events from changes within the Folder Chooser
        final BrowseInfoCallback proc = (wnd, msg, param, lpData) -> {
            if (msg == Shell32.BFFM_INITIALIZED && this.currentDirectory != null)
            {
                final String path = this.currentDirectory.getAbsolutePath ();
                final Pointer m = new Memory (Native.WCHAR_SIZE * (path.length () + 1));
                m.setWideString (0, path);
                User32.INSTANCE.PostMessage (new HWND (wnd), Shell32.BFFM_SETSELECTION, new WPARAM (1), new LPARAM (Pointer.nativeValue (m)));
            }
            return 0;
        };

        Ole32.INSTANCE.OleInitialize (null);
        final Shell32.BrowseInfo params = new Shell32.BrowseInfo ();

        params.hwndOwner = this.getParentWindow ();
        params.ulFlags = Shell32.BIF_RETURNONLYFSDIRS | Shell32.BIF_USENEWUI;
        if (title != null)
            params.lpszTitle = title;
        params.lpfn = proc;

        final Pointer pidl = Shell32.SHBrowseForFolder (params);
        if (pidl == null)
            return null;

        final Pointer path = new Memory (MAX_PATH_LENGTH);
        Shell32.SHGetPathFromIDListW (pidl, path);
        final File file = new File (path.getWideString (0));
        Ole32.INSTANCE.CoTaskMemFree (pidl);
        this.currentDirectory = file;
        return file;
    }


    /**
     * Displays the file browser.
     *
     * @param open Whether to show the open dialog, if false save dialog is shown
     * @param title The title for the dialog
     * @param filters Display only the files matching the given filters
     * @return The selected directory or null if the dialog was canceled
     * @throws IOException Could not create the dialog
     */
    private File showDialog (final boolean open, final String title, final FileFilter [] filters) throws IOException
    {
        final Comdlg32.OpenFileName params = this.configureParameters (title, filters);
        if (open ? Comdlg32.GetOpenFileNameW (params) : Comdlg32.GetSaveFileNameW (params))
        {
            final File selectedFile = params.getSelectedFile ();
            this.currentDirectory = selectedFile.getParentFile ();
            return selectedFile;
        }

        final int errCode = Comdlg32.CommDlgExtendedError ();
        // If the code is 0 the dialog was canceled
        if (errCode != 0)
            throw new IOException ("GetOpenFileName failed with error " + errCode);
        return null;
    }


    private Comdlg32.OpenFileName configureParameters (final String title, final FileFilter [] filters)
    {
        final Comdlg32.OpenFileName params = new Comdlg32.OpenFileName ();
        params.Flags = Comdlg32.OFN_EXPLORER | Comdlg32.OFN_NOCHANGEDIR | Comdlg32.OFN_HIDEREADONLY | Comdlg32.OFN_ENABLESIZING;
        params.hwndOwner = this.getParentWindow ();

        if (title != null)
            params.lpstrTitle = new WString (title);

        // lpstrFile contains the selection path after the dialog returns. It must be big enough for
        // the path to fit or GetOpenFileName returns an error (FNERR_BUFFERTOOSMALL). MAX_PATH is
        // 260 so 4*260+1 bytes should be big enough (I hope...)
        // http://msdn.microsoft.com/en-us/library/aa365247.aspx#maxpath
        final int bufferLength = 260;
        // 4 bytes per char + 1 null byte
        final int bufferSize = 4 * bufferLength + 1;
        params.lpstrFile = new Memory (bufferSize);
        params.lpstrFile.clear (bufferSize);

        // http://msdn.microsoft.com/en-us/library/ms646839.aspx:
        // "The size, in characters, of the buffer pointed to by lpstrFile. The buffer must be large
        // enough to store the path and file name string or strings, including the terminating NULL
        // character." For the unicode version of the API the nMaxFile value must be 1/4 of the
        // lpstrFile buffer
        // size plus one for the terminating null byte.
        params.nMaxFile = bufferLength;

        if (this.currentDirectory != null)
            params.lpstrInitialDir = this.currentDirectory.getAbsolutePath ();

        if (filters != null && filters.length > 0)
        {
            params.lpstrFilter = new WString (buildFilterString (filters));
            // Select the first filter
            params.nFilterIndex = 1;
        }
        return params;
    }


    /**
     * Build a filter string.
     *
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms646839.aspx">MSDN</a>
     * @param filters The filters to add
     * @return The created filter string
     */
    private static String buildFilterString (final FileFilter [] filters)
    {
        final StringBuilder filterStr = new StringBuilder ();
        for (final FileFilter spec: filters)
        {
            final StringBuilder exts = new StringBuilder ();
            final String [] extensions = spec.getExtensions ();
            for (int i = 0; i < extensions.length; i++)
            {
                if (i > 0)
                    exts.append (';');
                exts.append ("*.").append (extensions[i]);
            }
            filterStr.append (spec.getLabel ()).append (" (").append (exts).append (")\0");
            filterStr.append (exts).append ('\0');
        }
        return filterStr.append ('\0').toString ();
    }


    private Pointer getParentWindow ()
    {
        if (this.parentWindowClassName == null)
            return null;
        final HWND windowHandle = User32.INSTANCE.FindWindow (this.parentWindowClassName, null);
        return windowHandle == null ? null : windowHandle.getPointer ();
    }
}
