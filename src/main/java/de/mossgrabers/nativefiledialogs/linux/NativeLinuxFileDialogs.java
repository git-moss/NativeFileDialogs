// Written by Jürgen Moßgraber - mossgrabers.de
// (c) 2019
// Licensed under LGPLv3 - http://www.gnu.org/licenses/lgpl-3.0.txt

package de.mossgrabers.nativefiledialogs.linux;

import de.mossgrabers.nativefiledialogs.AbstractNativeFileDialogs;
import de.mossgrabers.nativefiledialogs.FileFilter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * The Linux implementation for the file dialogs.
 *
 * @author J&uuml;rgen Mo&szlig;graber
 */
public class NativeLinuxFileDialogs extends AbstractNativeFileDialogs
{
    private static boolean isZenityPresent = false;


    /**
     * Creates a new file dialog instance with the initial directory.
     *
     * @param currentDirectory The initial directory, may be null
     */
    public NativeLinuxFileDialogs (final File currentDirectory)
    {
        super (currentDirectory);

        detectZenity ();
    }


    /** {@inheritDoc} */
    @Override
    public File selectFile (final String title, final FileFilter... filters) throws IOException
    {
        final File file = selectFile (false, false, title, filters);
        return file != null && !file.isDirectory () ? file : null;
    }


    /** {@inheritDoc} */
    @Override
    public File selectNewFile (final String title, final FileFilter... filters) throws IOException
    {
        final File file = selectFile (false, true, title, filters);
        return file != null && !file.isDirectory () ? file : null;
    }


    /** {@inheritDoc} */
    @Override
    public File selectFolder (final String title) throws IOException
    {
        return selectFile (true, false, title, new FileFilter [0]);
    }


    private static File selectFile (final boolean isDirectory, final boolean doSave, final String title, final FileFilter... filters) throws IOException
    {
        if (!isZenityPresent)
            return null;

        final List<String> params = new ArrayList<> ();
        params.add ("zenity");
        params.add ("--file-selection");

        if (isDirectory)
            params.add ("--directory");

        if (doSave)
        {
            params.add ("--save");
            params.add ("--confirm-overwrite");
        }

        if (title != null && !title.isEmpty ())
            params.add (String.format ("--title=%s", title));

        if (filters.length > 0)
        {
            for (final FileFilter filter: filters)
            {
                final StringBuilder sb = new StringBuilder ();
                sb.append ("--file-filter=").append (filter.getLabel ()).append (" | ");
                for (final String extension: filter.getExtensions ())
                    sb.append ("*.").append (extension).append (" ");
                params.add (sb.toString ().trim ());
            }
            params.add ("--file-filter=All files | *");
        }
        // Suppress warnings
        params.add ("2>/dev/null");

        final String result = executeProcess (params.toArray (new String [params.size ()]));
        if (result.isEmpty ())
            return null;

        return new File (result);
    }


    private static void detectZenity ()
    {
        try
        {
            final String result = executeProcess (new String []
            {
                "which",
                "zenity",
                // Suppress warnings
                "2>/dev/null"
            });
            if (!result.isEmpty ())
                isZenityPresent = new File (result).exists ();
        }
        catch (final IOException ex)
        {
            return;
        }
    }
}
