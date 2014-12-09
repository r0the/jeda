/*
 * Copyright (C) 2013 - 2014 by Stefan Rothe
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

class SpeechBubble {

    private static final String SPEECH_PREFIX = "says:";
    private static final Image INFO_BUBBLE = Cute.loadImage("info_bubble");
    private static final Image SPEECH_BUBBLE = Cute.loadImage("speech_bubble");
    private static final int BORDER = 10;
    private static final int LINE_HEIGHT = 20;
    private static final int TEXT_SIZE = 20;

    static void draw(final Canvas canvas, double x, double y, String message) {
        final Image image = imageFor(message);
        if (message.startsWith(SPEECH_PREFIX)) {
            message = message.substring(SPEECH_PREFIX.length());
        }

        canvas.drawImage(x, y, image, Alignment.BOTTOM_CENTER);
        canvas.setColor(Color.BLACK);
        canvas.setTextSize(TEXT_SIZE);
        final List<String> lines = lineBreak(canvas, message, image.getWidth() - 2 * BORDER);
        y -= 204;
        x = x - image.getWidth() / 2 + BORDER;
        for (int i = 0; i < lines.size(); ++i) {
            canvas.drawText(x, y, lines.get(i), Alignment.TOP_LEFT);
            y += LINE_HEIGHT;
        }
    }

    private static Image imageFor(final String message) {
        if (message.startsWith(SPEECH_PREFIX)) {
            return SPEECH_BUBBLE;
        }
        else {
            return INFO_BUBBLE;
        }
    }

    private static List<String> lineBreak(final Canvas canvas, final String message, final int maxWidth) {
        final String[] words = message.split(" ");
        final List<String> result = new ArrayList<String>();

        String line = "";
        String lookahead = null;
        for (int i = 0; i < words.length; ++i) {
            lookahead = line;
            if (!lookahead.isEmpty()) {
                lookahead = lookahead + " ";
            }

            lookahead = lookahead + words[i];
            if (canvas.textWidth(lookahead) < maxWidth) {
                line = lookahead;
            }
            else {
                result.add(line);
                line = words[i];
            }
        }

        result.add(line);
        return result;
    }
}
