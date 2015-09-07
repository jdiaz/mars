package com.scriptfuzz.blog;

import com.github.rjeschke.txtmark.BlockEmitter;

import java.util.List;

/**
 * Created by zeek on 09-06-15.
 */
public class CodeBlockEmitter implements BlockEmitter
{
    private static void append(StringBuilder out, List<String> lines)
    {
        out.append("<pre>");
        for (final String l : lines)
        {
            //Utils.escapedAdd(out, l);
            out.append(l);
            System.out.println(l);
            out.append('\n');
        }
        out.append("</pre>");
    }

    @Override
    public void emitBlock(StringBuilder out, List<String> lines, String meta)
    {
        System.out.println("Emit Block working!");
        if (meta.isEmpty())
        {
            append(out, lines);
        }
        else
        {
            try
            {
                // Utils#highlight(...) is not included with txtmark, it's sole purpose
                // is to show what the meta can be used for
                //out.append(Utils.highlight(lines, meta));
                out.append('\n');
            }
            catch (final Exception e)
            {
                // Ignore or do something, still, pump out the lines
                append(out, lines);
            }
        }
    }
}
