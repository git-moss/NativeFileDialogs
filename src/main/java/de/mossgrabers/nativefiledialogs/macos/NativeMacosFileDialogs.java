// Written by Jürgen Moßgraber - mossgrabers.de
// (c) 2019-2026
// Licensed under LGPLv3 - http://www.gnu.org/licenses/lgpl-3.0.txt

package de.mossgrabers.nativefiledialogs.macos;

import de.mossgrabers.nativefiledialogs.AbstractNativeFileDialogs;
import de.mossgrabers.nativefiledialogs.FileFilter;
import de.mossgrabers.nativefiledialogs.ProcessResult;

import java.io.File;
import java.io.IOException;


/**
 * The Macos implementation for the file dialogs. Triggers file dialogs via Applescript.
 *
 * @author J&uuml;rgen Mo&szlig;graber
 */
public class NativeMacosFileDialogs extends AbstractNativeFileDialogs
{
    private static final String FOLDER_START = "tell application \"Finder\"\nActivate\ntry\nPOSIX path of ( choose folder ";
    private static final String OPEN_START   = "tell application \"Finder\"\nActivate\ntry\nPOSIX path of ( choose file ";
    private static final String SAVE_START   = "tell application \"Finder\"\nActivate\ntry\nPOSIX path of ( choose file name ";
    private static final String END          = ")\non error number -128\nend try\nend tell";

    /**
     * Creates a new file dialog instance with the initial directory.
     *
     * @param currentDirectory The initial directory, may be null
     */
    public NativeMacosFileDialogs (final File currentDirectory)
    {
        super (currentDirectory);
    }


    /** {@inheritDoc} */
    @Override
    public File selectFile (final String title, final FileFilter... filters) throws IOException
    {
        final StringBuilder applescriptCommand = new StringBuilder (OPEN_START);
        this.addTitleAndDirectory (applescriptCommand, title, false, false);

        if (filters.length > 0)
        {
            applescriptCommand.append ("of type {\"");
            applescriptCommand.append (filters[0].getLabel ());
            applescriptCommand.append ("\"");
            final String [] extensions = filters[0].getExtensions ();
            for (final String extension: extensions)
            {
                applescriptCommand.append (",\"");
                applescriptCommand.append (extension);
                applescriptCommand.append ("\"");
            }
            applescriptCommand.append ("} ");
        }

        applescriptCommand.append (END);

        final String filename = runApplescript (applescriptCommand.toString ());
        return filename.isEmpty () ? null : new File (filename);
    }


    /** {@inheritDoc} */
    @Override
    public File selectNewFile (final String title, final FileFilter... filters) throws IOException
    {
        final StringBuilder applescriptCommand = new StringBuilder (SAVE_START);
        this.addTitleAndDirectory (applescriptCommand, title, false, true);
        applescriptCommand.append (END);

        final String filename = runApplescript (applescriptCommand.toString ());
        return filename.isEmpty () ? null : new File (filename);
    }


    /** {@inheritDoc} */
    @Override
    public File selectFolder (final String title) throws IOException
    {
        final StringBuilder applescriptCommand = new StringBuilder (FOLDER_START);
        this.addTitleAndDirectory (applescriptCommand, title, true, false);
        applescriptCommand.append (END);

        final String filename = runApplescript (applescriptCommand.toString ());
        return filename.isEmpty () ? null : new File (filename);
    }


    /**
     * Add the title and current directory to the script.
     *
     * @param applescriptCommand The apple script
     * @param title The title
     * @param onlyFolder True if only the folder should be added as pre-selection
     * @param addFilename True to add the name as pre-selection
     */
    private void addTitleAndDirectory (final StringBuilder applescriptCommand, final String title, final boolean onlyFolder, final boolean addFilename)
    {
        if (title != null && !title.isEmpty ())
            applescriptCommand.append (String.format ("with prompt \"%s\" ", title));

        if (this.currentDirectory == null)
            return;
        File dir = this.currentDirectory;
        if (!dir.exists ())
            return;
        if (onlyFolder)
            dir = this.currentDirectory.isDirectory () ? this.currentDirectory : this.currentDirectory.getParentFile ();
        applescriptCommand.append (String.format ("default location \"%s\" ", dir.getAbsolutePath ()));

        if (addFilename && this.currentDirectory.isFile ())
            applescriptCommand.append (String.format ("default name \"%s\" ", this.currentDirectory.getName ()));
    }


    /**
     * Run an apple script command und returns the result.
     *
     * @param command The Apple script command to execute
     * @return THe result
     * @throws IOException Could not execute the command
     */
    private static String runApplescript (final String command) throws IOException
    {
        final ProcessResult processResult = executeProcess (new String []
        {
            "osascript",
            "-e",
            command
        });

        final String error = processResult.getError ();
        if (!error.isEmpty ())
            throw new IOException (error);

        return processResult.getResult ();
    }
}
