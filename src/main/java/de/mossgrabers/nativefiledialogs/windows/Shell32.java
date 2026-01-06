// Written by Jürgen Moßgraber - mossgrabers.de
// (c) 2019-2026
// Licensed under LGPLv3 - http://www.gnu.org/licenses/lgpl-3.0.txt

package de.mossgrabers.nativefiledialogs.windows;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.win32.StdCallLibrary;

import java.util.Arrays;
import java.util.List;


/**
 * Mapping for the necessary Shell32 functions (sadly these functions are not mapped by the
 * jna-platform class.
 *
 * @author J&uuml;rgen Mo&szlig;graber
 */
public class Shell32
{
    static
    {
        Native.register ("shell32");
    }

    /**
     * Only return file system directories. If the user selects folders that are not part of the
     * file system, the OK button is grayed.
     */
    public static final int BIF_RETURNONLYFSDIRS    = 0x00000001;
    /**
     * Do not include network folders below the domain level in the dialog box's tree view control.
     */
    public static final int BIF_DONTGOBELOWDOMAIN   = 0x00000002;
    /**
     * Include an edit control in the browse dialog box that allows the user to type the name of an
     * item.
     */
    public static final int BIF_EDITBOX             = 0x00000010;
    /**
     * Use the new user interface. Setting this flag provides the user with a larger dialog box that
     * can be resized. The dialog box has several new capabilities, including: drag-and-drop
     * capability within the dialog box, reordering, shortcut menus, new folders, delete, and other
     * shortcut menu commands.
     */
    public static final int BIF_NEWDIALOGSTYLE      = 0x00000040;
    /**
     * Use the new user interface, including an edit box. This flag is equivalent to BIF_EDITBOX |
     * BIF_NEWDIALOGSTYLE.
     */
    public static final int BIF_USENEWUI            = BIF_EDITBOX | BIF_NEWDIALOGSTYLE;
    /** Do not include the New Folder button in the browse dialog box. */
    public static final int BIF_NONEWFOLDERBUTTON   = 0x00000200;
    /** The browse dialog box displays files as well as folders. */
    public static final int BIF_BROWSEINCLUDEFILES  = 0x00004000;
    /**
     * The browse dialog box can display sharable resources on remote systems. This is intended for
     * applications that want to expose remote shares on a local system. The BIF_NEWDIALOGSTYLE flag
     * must also be set.
     */
    public static final int BIF_SHAREABLE           = 0x00008000;
    /**
     * Allow folder junctions such as a library or a compressed file with a .zip file name extension
     * to be browsed.
     */
    public static final int BIF_BROWSEFILEJUNCTIONS = 0x00010000;

    /** The dialog box has finished initializing. */
    public static final int BFFM_INITIALIZED        = 1;
    /**
     * Specifies the path of a folder to select. The path can be specified as a string or a PIDL.
     */
    public static final int BFFM_SETSELECTION       = 1024 + 103;

    /**
     * Displays a dialog box that enables the user to select a Shell folder.
     *
     * @param params A pointer to a BROWSEINFO structure that contains information used to display
     *            the dialog box.
     * @return Returns a PIDL that specifies the location of the selected folder relative to the
     *         root of the namespace. If the user chooses the Cancel button in the dialog box, the
     *         return value is NULL.It is possible that the PIDL returned is that of a folder
     *         shortcut rather than a folder. For a full discussion of this case, see the Remarks
     *         section.
     */
    public static native Pointer SHBrowseForFolder (BrowseInfo params);


    /**
     * Converts an item identifier list to a file system path.
     *
     * @param pidl The address of an item identifier list that specifies a file or directory
     *            location relative to the root of the namespace (the desktop).
     * @param path The address of a buffer to receive the file system path. This buffer must be at
     *            least MAX_PATH characters in size.
     * @return Returns TRUE if successful; otherwise, FALSE.
     */
    public static native boolean SHGetPathFromIDListW (Pointer pidl, Pointer path);

    /**
     * Contains parameters for the SHBrowseForFolder function and receives information about the
     * folder selected by the user.
     */
    public static class BrowseInfo extends Structure
    {
        /** A handle to the owner window for the dialog box. */
        public Pointer            hwndOwner;
        /**
         * A PIDL that specifies the location of the root folder from which to start browsing. Only
         * the specified folder and its subfolders in the namespace hierarchy appear in the dialog
         * box. This member can be NULL; in that case, a default location is used.
         */
        public Pointer            pidlRoot;
        /**
         * Pointer to a buffer to receive the display name of the folder selected by the user. The
         * size of this buffer is assumed to be MAX_PATH characters.
         */
        public String             pszDisplayName;
        /**
         * Pointer to a null-terminated string that is displayed above the tree view control in the
         * dialog box. This string can be used to specify instructions to the user.
         */
        public String             lpszTitle;
        /**
         * Flags that specify the options for the dialog box. This member can be 0 or a combination
         * of the following values. Version numbers refer to the minimum version of Shell32.dll
         * required for SHBrowseForFolder to recognize flags added in later releases. See Shell and
         * Common Controls Versions for more information.
         */
        public int                ulFlags;
        /**
         * Pointer to an application-defined function that the dialog box calls when an event
         * occurs. For more information, see the BrowseCallbackProc function. This member can be
         * NULL.
         */
        public BrowseInfoCallback lpfn;
        /**
         * An application-defined value that the dialog box passes to the callback function, if one
         * is specified in lpfn.
         */
        public Pointer            lParam;
        /**
         * An integer value that receives the index of the image associated with the selected
         * folder, stored in the system image list.
         */
        public int                iImage;

        /** {@inheritDoc} */
        @Override
        protected List<String> getFieldOrder ()
        {
            return Arrays.asList ("hwndOwner", "pidlRoot", "pszDisplayName", "lpszTitle", "ulFlags", "lpfn", "lParam", "iImage");
        }
    }

    /**
     * A callback function for receiving change events from a Dialog.
     */
    public interface BrowseInfoCallback extends StdCallLibrary.StdCallCallback
    {
        /**
         * The dialog box calls this function when an event occurs.
         *
         * @param hWnd The window handle
         * @param uMsg The message
         * @param lParam The long parameter
         * @param lpData The data
         * @return The result
         */
        int callback (Pointer hWnd, int uMsg, int lParam, int lpData);
    }
}
