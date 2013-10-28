/*
 * Copyright (C) 2013 by Stefan Rothe
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY); without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package ch.jeda.cute;

import ch.jeda.ui.Alignment;
import ch.jeda.ui.Canvas;
import ch.jeda.ui.Color;
import ch.jeda.ui.Image;
import java.util.ArrayList;
import java.util.List;

public class SpeechBubble {

    private static final String SPEECH_PREFIX = "says:";
    private static final Image INFO_BUBBLE = Cute.loadImage("info_bubble");
    private static final Image SPEECH_BUBBLE = Cute.loadImage("speech_bubble");

    static void draw(final Canvas canvas, float x, float y, String message) {
        if (message.startsWith(SPEECH_PREFIX)) {
            canvas.drawImage(x, y, SPEECH_BUBBLE, Alignment.BOTTOM_CENTER);
            message = message.substring(SPEECH_PREFIX.length());
        }
        else {
            canvas.drawImage(x, y, INFO_BUBBLE, Alignment.BOTTOM_CENTER);
        }

        canvas.setColor(Color.BLACK);
        final List<String> lines = lineBreak(canvas, message, 90);
        y -= 204f;
        x = x - 50f + 5f;
        for (final String line : lines) {
            canvas.drawText(x, y, line, Alignment.TOP_LEFT);
            y += 20f;
        }
    }

    private static List<String> lineBreak(final Canvas canvas, final String message, final int maxWidth) {
        final String[] words = message.split(" ");
        final List<String> result = new ArrayList<String>();

        int i = 0;
        while (i < words.length) {
            String lookahead = words[i];
            String line = null;
            while ((canvas.textWidth(lookahead) < maxWidth) && (i + 1 < words.length)) {
                line = lookahead;
                i++;
                lookahead = line + " " + words[i];
            }

            if (line == null) {
                line = lookahead;
                i++;
            }

            result.add(line);
        }
        return result;
    }
}
