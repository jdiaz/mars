package com.scriptfuzz.blog;

import com.github.rjeschke.txtmark.BlockEmitter;

import java.util.List;

/**
 * Created by zeek on 09-06-15.
 */
public class CodeBlockEmitter implements BlockEmitter {

    private static void append(StringBuilder out, List<String> lines) {
        out.append("<pre>");
        for (final String l : lines) {
            out.append(l);
            out.append('\n');
        }
        out.append("</pre>");
    }

    @Override
    public void emitBlock(StringBuilder out, List<String> lines, String meta) {

        if (meta.isEmpty()) {
            append(out, lines);
        }
        else {
            try {
                out.append('\n');
            }
            catch (final Exception e) {
                // Ignore or do something, still, pump out the lines
                append(out, lines);
            }
        }
    }
}
