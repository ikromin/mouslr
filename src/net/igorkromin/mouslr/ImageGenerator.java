/**
 * Mouslr - Mouse Tracking Pixel Art!
 * http://mouslr.kr0m.in
 * Copyright (C) 2015  Igor Kromin
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; version 2 of the License.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * You can find this and my other open source projects here - http://github.com/ikromin
 */

package net.igorkromin.mouslr;

import java.io.File;
import java.io.IOException;

/**
 * Created by ikromin on 8/07/2015.
 */
public interface ImageGenerator {

    String MOUSLR_HEADER = "MOUSLR";

    void generate(File inputData, File outputFile) throws IOException;

}
