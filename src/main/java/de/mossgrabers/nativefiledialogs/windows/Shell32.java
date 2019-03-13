// Written by Jürgen Moßgraber - mossgrabers.de
// (c) 2019
// Licensed under LGPLv3 - http://www.gnu.org/licenses/lgpl-3.0.txt

package de.mossgrabers.nativefiledialogs.windows;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;

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

    // Flags for the BrowseInfo structure

    /** Disable the OK button if the user selects a virtual PIDL. */
    public static final int BIF_RETURNONLYFSDIRS    = 0x00000001;
    public static final int BIF_DONTGOBELOWDOMAIN   = 0x00000002;
    public static final int BIF_NEWDIALOGSTYLE      = 0x00000040;
    public static final int BIF_EDITBOX             = 0x00000010;
    public static final int BIF_USENEWUI            = BIF_EDITBOX | BIF_NEWDIALOGSTYLE;
    public static final int BIF_NONEWFOLDERBUTTON   = 0x00000200;
    public static final int BIF_BROWSEINCLUDEFILES  = 0x00004000;
    public static final int BIF_SHAREABLE           = 0x00008000;
    public static final int BIF_BROWSEFILEJUNCTIONS = 0x00010000;


    public static native Pointer SHBrowseForFolder (BrowseInfo params);


    public static native boolean SHGetPathFromIDListW (Pointer pidl, Pointer path);

    // http://msdn.microsoft.com/en-us/library/bb773205.aspx
    public static class BrowseInfo extends Structure
    {
        public Pointer hwndOwner;
        public Pointer pidlRoot;
        public String  pszDisplayName;
        public String  lpszTitle;
        public int     ulFlags;
        public Pointer lpfn;
        public Pointer lParam;
        public int     iImage;


        /** {@inheritDoc} */
        @Override
        protected List<String> getFieldOrder ()
        {
            return Arrays.asList ("hwndOwner", "pidlRoot", "pszDisplayName", "lpszTitle", "ulFlags", "lpfn", "lParam", "iImage");
        }
    }
}
