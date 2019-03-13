// Written by Jürgen Moßgraber - mossgrabers.de
// (c) 2019
// Licensed under LGPLv3 - http://www.gnu.org/licenses/lgpl-3.0.txt

package de.mossgrabers.nativefiledialogs;

import java.io.File;


/**
 * Interface to various file selection dialogs.
 *
 * @author J&uuml;rgen Mo&szlig;graber
 */
public interface NativeFileDialogs
{
    /**
     * Display a dialog for selecting a fil, which should be opened.
     *
     * @param filters Display only the files matching the given filters
     * @return The selected file or null if the dialog was canceled
     */
    File selectFile (FileFilter... filters);


    /**
     * Display a dialog for selecting a fil, which should be saved.
     *
     * @param filters Display only the files matching the given filters
     * @return The selected file or null if the dialog was canceled
     */
    File selectNewFile (FileFilter... filters);


    /**
     * Display a dialog to select a folder.
     *
     * @param title The title to use for the folder, may be null
     * @return The selected folder or null if the dialog was canceled
     */
    File selectFolder (String title);
}
