// Written by Jürgen Moßgraber - mossgrabers.de
// (c) 2019
// Licensed under LGPLv3 - http://www.gnu.org/licenses/lgpl-3.0.txt

package de.mossgrabers.nativefiledialogs;

import java.io.File;
import java.io.IOException;


/**
 * Interface to various file selection dialogs.
 *
 * @author J&uuml;rgen Mo&szlig;graber
 */
public interface NativeFileDialogs
{
    /**
     * Set the curent directory.
     *
     * @param currentDirectory The current directory
     */
    void setCurrentDirectory (File currentDirectory);


    /**
     * Display a dialog for selecting a fil, which should be opened.
     *
     * @return The selected file or null if the dialog was canceled
     * @throws IOException A problem with the dialog appeared
     */
    File selectFile () throws IOException;


    /**
     * Display a dialog for selecting a fil, which should be opened.
     *
     * @param filters Display only the files matching the given filters
     * @return The selected file or null if the dialog was canceled
     * @throws IOException A problem with the dialog appeared
     */
    File selectFile (FileFilter... filters) throws IOException;


    /**
     * Display a dialog for selecting a fil, which should be opened.
     *
     * @param title The title to use for the folder, may be null
     * @param filters Display only the files matching the given filters
     * @return The selected file or null if the dialog was canceled
     * @throws IOException A problem with the dialog appeared
     */
    File selectFile (String title, FileFilter... filters) throws IOException;


    /**
     * Display a dialog for selecting a fil, which should be saved.
     *
     * @return The selected file or null if the dialog was canceled
     * @throws IOException A problem with the dialog appeared
     */
    File selectNewFile () throws IOException;


    /**
     * Display a dialog for selecting a fil, which should be saved.
     *
     * @param filters Display only the files matching the given filters
     * @return The selected file or null if the dialog was canceled
     * @throws IOException A problem with the dialog appeared
     */
    File selectNewFile (FileFilter... filters) throws IOException;


    /**
     * Display a dialog for selecting a fil, which should be saved.
     *
     * @param title The title to use for the folder, may be null
     * @param filters Display only the files matching the given filters
     * @return The selected file or null if the dialog was canceled
     * @throws IOException A problem with the dialog appeared
     */
    File selectNewFile (String title, FileFilter... filters) throws IOException;


    /**
     * Display a dialog to select a folder.
     *
     * @return The selected folder or null if the dialog was canceled
     * @throws IOException A problem with the dialog appeared
     */
    File selectFolder () throws IOException;


    /**
     * Display a dialog to select a folder.
     *
     * @param title The title to use for the folder, may be null
     * @return The selected folder or null if the dialog was canceled
     * @throws IOException A problem with the dialog appeared
     */
    File selectFolder (String title) throws IOException;
}
