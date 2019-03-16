// Written by Jürgen Moßgraber - mossgrabers.de
// (c) 2019
// Licensed under LGPLv3 - http://www.gnu.org/licenses/lgpl-3.0.txt

package de.mossgrabers.nativefiledialogs.macos;

import de.mossgrabers.nativefiledialogs.AbstractNativeFileDialogs;
import de.mossgrabers.nativefiledialogs.FileFilter;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;


/**
 * The Macos implementation for the file dialogs.
 *
 * @author J&uuml;rgen Mo&szlig;graber
 */
public class NativeMacosFileDialogs extends AbstractNativeFileDialogs
{
    private static final String FOLDER_START = "tell application \"Finder\"\nActivate\ntry\nPOSIX path of ( choose folder ";
    private static final String OPEN_START   = "tell application \"Finder\"\nActivate\ntry\nPOSIX path of ( choose file ";
    private static final String SAVE_START   = "tell application \"Finder\"\nActivate\ntry\nPOSIX path of ( choose file name ";
    private static final String END          = ")\non error number -128\nend try\nend tell";

    private File                currentDirectory;


    /**
     * Creates a new file dialog instance with the initial directory.
     *
     * @param currentDirectory The initial directory, may be null
     */
    public NativeMacosFileDialogs (final File currentDirectory)
    {
        this.currentDirectory = currentDirectory;
    }


    /** {@inheritDoc} */
    @Override
    public File selectFile (final String title, final FileFilter... filters) throws IOException
    {
        final StringBuilder applescriptCommand = new StringBuilder (OPEN_START);

        if (title != null)
            applescriptCommand.append (String.format ("with prompt \"%s\" ", title));

        if (this.currentDirectory != null)
            applescriptCommand.append (String.format ("default location \"%s\" ", this.currentDirectory.getAbsolutePath ()));

        if (filters.length > 0)
        {
            applescriptCommand.append ("of type {\"");
            applescriptCommand.append (filters[0].getLabel ()) ;
            applescriptCommand.append ("\"" );
            final String [] extensions = filters[0].getExtensions ();
            for (int i = 0 ; i < extensions.length ; i++)
            {
                applescriptCommand.append (",\"" ) ;
                applescriptCommand.append (extensions [i]) ;
                applescriptCommand.append ("\"" ) ;
            }
            applescriptCommand.append ("} " ) ;
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

        if (title != null)
            applescriptCommand.append (String.format ("with prompt \"%s\" ", title));

        if (this.currentDirectory != null)
            applescriptCommand.append (String.format ("default location \"%s\" ", this.currentDirectory.getAbsolutePath ()));
        
        applescriptCommand.append (END);

        final String filename = runApplescript (applescriptCommand.toString ());
        return filename.isEmpty () ? null : new File (filename);
    }


    /** {@inheritDoc} */
    @Override
    public File selectFolder (final String title) throws IOException
    {
        final StringBuilder applescriptCommand = new StringBuilder (FOLDER_START);

        if (title != null)
            applescriptCommand.append (String.format ("with prompt \"%s\" ", title));

        if (this.currentDirectory != null)
            applescriptCommand.append (String.format ("default location \"%s\" ", this.currentDirectory.getAbsolutePath ()));
        
        applescriptCommand.append (END);

        final String filename = runApplescript (applescriptCommand.toString ());
        return filename.isEmpty () ? null : new File (filename);        
    }


    private static String runApplescript (final String command) throws IOException
    {
        final String [] args =
        {
            "osascript",
            "-e",
            command
        };

        final ProcessBuilder pb = new ProcessBuilder (Arrays.asList (args));
        pb.redirectErrorStream (true);

        final StringBuilder result = new StringBuilder ();

        // Start the process
        final Process proc = pb.start ();

        // Read the process's output
        try (final BufferedReader in = new BufferedReader (new InputStreamReader (proc.getInputStream ())))
        {
            String line;
            while ((line = in.readLine ()) != null)
                result.append (line);
        }
        finally
        {
            proc.destroy ();
        }

        return result.toString ().trim ();
    }
}
