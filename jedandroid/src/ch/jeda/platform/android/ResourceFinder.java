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
package ch.jeda.platform.android;

import android.app.Activity;
import dalvik.system.DexFile;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class ResourceFinder {

    private final Activity activity;
    private final List<String> classNames;
    private boolean done;

    ResourceFinder(Activity activity) {
        this.activity = activity;
        this.classNames = new ArrayList();
        this.done = false;
    }

    List<String> findClassNames() throws Exception {
        this.ensureResources();
        return this.classNames;
    }

    private void ensureResources() throws Exception {
        if (!this.done) {
            this.findResources();
            this.done = true;
        }
    }

    private void findResources() throws Exception {
        String packageName = this.activity.getApplicationInfo().packageName;
        String apkName = this.activity.getPackageManager().getApplicationInfo(packageName, 0).sourceDir;
        DexFile dx = new DexFile(apkName);
        Enumeration<String> e = dx.entries();
        while (e.hasMoreElements()) {
            String resourceName = e.nextElement();
            if (!resourceName.contains("$")) {
                this.classNames.add(resourceName);
            }
        }
    }
}
