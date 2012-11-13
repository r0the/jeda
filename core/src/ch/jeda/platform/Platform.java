/*
 * Copyright (C) 2012 by Stefan Rothe
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
package ch.jeda.platform;

import ch.jeda.Size;
import java.net.URL;

public interface Platform {

    CanvasImp createCanvasImp(Size size);

    Iterable<String> listClassNames() throws Exception;

    Iterable<String> listPropertyFiles() throws Exception;

    ImageImp loadImageImp(URL url) throws Exception;

    void showInputRequest(InputRequest inputRequest);

    void showSelectionRequest(SelectionRequest selectionRequest);

    void showLog(LogInfo logInfo);

    ViewImp showView(ViewInfo viewInfo);

    void stop();
}
