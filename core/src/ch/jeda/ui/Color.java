/*
 * Copyright (C) 2011 - 2015 by Stefan Rothe
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
package ch.jeda.ui;

import ch.jeda.Data;
import ch.jeda.Storable;
import java.io.Serializable;

/**
 * Represents a color in the RGBA color model. A color is defined by three values for the red, green, and blue part, and
 * an alpha value defining the color's opacity. Each value can be a number between 0 and 255.
 *
 * @since 1.0
 * @version 2
 */
public final class Color implements Serializable, Storable {

    private static final String R = "r";
    private static final String G = "g";
    private static final String B = "b";
    private static final String A = "a";

    /**
     * The Google material design color <i>Amber 50</i>.
     *
     * @since 2.1
     */
    public static final Color AMBER_50 = new Color("#fff8e1");

    /**
     * The Google material design color <i>Amber 100</i>.
     *
     * @since 2.1
     */
    public static final Color AMBER_100 = new Color("#ffecb3");

    /**
     * The Google material design color <i>Amber 200</i>.
     *
     * @since 2.1
     */
    public static final Color AMBER_200 = new Color("#ffe082");

    /**
     * The Google material design color <i>Amber 300</i>.
     *
     * @since 2.1
     */
    public static final Color AMBER_300 = new Color("#ffd54f");

    /**
     * The Google material design color <i>Amber 400</i>.
     *
     * @since 2.1
     */
    public static final Color AMBER_400 = new Color("#ffca28");

    /**
     * The Google material design color <i>Amber 500</i>.
     *
     * @since 2.1
     */
    public static final Color AMBER_500 = new Color("#ffc107");

    /**
     * The Google material design color <i>Amber 600</i>.
     *
     * @since 2.1
     */
    public static final Color AMBER_600 = new Color("#ffb300");

    /**
     * The Google material design color <i>Amber 700</i>.
     *
     * @since 2.1
     */
    public static final Color AMBER_700 = new Color("#ffa000");

    /**
     * The Google material design color <i>Amber 800</i>.
     *
     * @since 2.1
     */
    public static final Color AMBER_800 = new Color("#ff8f00");

    /**
     * The Google material design color <i>Amber 900</i>.
     *
     * @since 2.1
     */
    public static final Color AMBER_900 = new Color("#ff6f00");

    /**
     * The Google material design color <i>Amber A100</i>.
     *
     * @since 2.1
     */
    public static final Color AMBER_A100 = new Color("#ffe57f");

    /**
     * The Google material design color <i>Amber A200</i>.
     *
     * @since 2.1
     */
    public static final Color AMBER_A200 = new Color("#ffd740");

    /**
     * The Google material design color <i>Amber A400</i>.
     *
     * @since 2.1
     */
    public static final Color AMBER_A400 = new Color("#ffc400");

    /**
     * The Google material design color <i>Amber A700</i>.
     *
     * @since 2.1
     */
    public static final Color AMBER_A700 = new Color("#ffab00");

    /**
     * The VGA color <i>aqua</i>. Same as <code>new Color(0, 255, 255)</code>.
     *
     * @since 1.0
     */
    public static final Color AQUA = new Color(0, 255, 255);

    /**
     * The VGA color <i>black</i>. Same as <code>new Color(0, 0, 0)</code>.
     *
     * @since 1.0
     */
    public static final Color BLACK = new Color(0, 0, 0);

    /**
     * The VGA color <i>blue</i>. Same as <code>new Color(0, 0, 255)</code>.
     *
     * @since 1.0
     */
    public static final Color BLUE = new Color(0, 0, 255);

    /**
     * The Google material design color <i>Blue 50</i>.
     *
     * @since 2.1
     */
    public static final Color BLUE_50 = new Color("#e3f2fd");

    /**
     * The Google material design color <i>Blue 100</i>.
     *
     * @since 2.1
     */
    public static final Color BLUE_100 = new Color("#bbdefb");

    /**
     * The Google material design color <i>Blue 200</i>.
     *
     * @since 2.1
     */
    public static final Color BLUE_200 = new Color("#90caf9");

    /**
     * The Google material design color <i>Blue 300</i>.
     *
     * @since 2.1
     */
    public static final Color BLUE_300 = new Color("#64b5f6");

    /**
     * The Google material design color <i>Blue 400</i>.
     *
     * @since 2.1
     */
    public static final Color BLUE_400 = new Color("#42a5f5");

    /**
     * The Google material design color <i>Blue 500</i>.
     *
     * @since 2.1
     */
    public static final Color BLUE_500 = new Color("#2196f3");

    /**
     * The Google material design color <i>Blue 600</i>.
     *
     * @since 2.1
     */
    public static final Color BLUE_600 = new Color("#1e88e5");

    /**
     * The Google material design color <i>Blue 700</i>.
     *
     * @since 2.1
     */
    public static final Color BLUE_700 = new Color("#1976d2");

    /**
     * The Google material design color <i>Blue 800</i>.
     *
     * @since 2.1
     */
    public static final Color BLUE_800 = new Color("#1565c0");

    /**
     * The Google material design color <i>Blue 900</i>.
     *
     * @since 2.1
     */
    public static final Color BLUE_900 = new Color("#0d47a1");

    /**
     * The Google material design color <i>Blue A100</i>.
     *
     * @since 2.1
     */
    public static final Color BLUE_A100 = new Color("#82b1ff");

    /**
     * The Google material design color <i>Blue A200</i>.
     *
     * @since 2.1
     */
    public static final Color BLUE_A200 = new Color("#448aff");

    /**
     * The Google material design color <i>Blue A400</i>.
     *
     * @since 2.1
     */
    public static final Color BLUE_A400 = new Color("#2979ff");

    /**
     * The Google material design color <i>Blue A700</i>.
     *
     * @since 2.1
     */
    public static final Color BLUE_A700 = new Color("#2962ff");

    /**
     * The Google material design color <i>Brown 50</i>.
     *
     * @since 2.1
     */
    public static final Color BROWN_50 = new Color("#efebe9");

    /**
     * The Google material design color <i>Brown 100</i>.
     *
     * @since 2.1
     */
    public static final Color BROWN_100 = new Color("#d7ccc8");

    /**
     * The Google material design color <i>Brown 200</i>.
     *
     * @since 2.1
     */
    public static final Color BROWN_200 = new Color("#bcaaa4");

    /**
     * The Google material design color <i>Brown 300</i>.
     *
     * @since 2.1
     */
    public static final Color BROWN_300 = new Color("#a1887f");

    /**
     * The Google material design color <i>Brown 400</i>.
     *
     * @since 2.1
     */
    public static final Color BROWN_400 = new Color("#8d6e63");

    /**
     * The Google material design color <i>Brown 500</i>.
     *
     * @since 2.1
     */
    public static final Color BROWN_500 = new Color("#795548");

    /**
     * The Google material design color <i>Brown 600</i>.
     *
     * @since 2.1
     */
    public static final Color BROWN_600 = new Color("#6d4c41");

    /**
     * The Google material design color <i>Brown 700</i>.
     *
     * @since 2.1
     */
    public static final Color BROWN_700 = new Color("#5d4037");

    /**
     * The Google material design color <i>Brown 800</i>.
     *
     * @since 2.1
     */
    public static final Color BROWN_800 = new Color("#4e342e");

    /**
     * The Google material design color <i>Brown 900</i>.
     *
     * @since 2.1
     */
    public static final Color BROWN_900 = new Color("#3e2723");

    /**
     * The Google material design color <i>Cyan 50</i>.
     *
     * @since 2.0
     */
    public static final Color CYAN_50 = new Color("#e0f7fa");

    /**
     * The Google material design color <i>Cyan 100</i>.
     *
     * @since 2.0
     */
    public static final Color CYAN_100 = new Color("#b2ebf2");

    /**
     * The Google material design color <i>Cyan 200</i>.
     *
     * @since 2.0
     */
    public static final Color CYAN_200 = new Color("#80deea");

    /**
     * The Google material design color <i>Cyan 300</i>.
     *
     * @since 2.0
     */
    public static final Color CYAN_300 = new Color("#4dd0e1");

    /**
     * The Google material design color <i>Cyan 400</i>.
     *
     * @since 2.0
     */
    public static final Color CYAN_400 = new Color("#26c6da");

    /**
     * The Google material design color <i>Cyan 500</i>.
     *
     * @since 2.0
     */
    public static final Color CYAN_500 = new Color("#00bcd4");

    /**
     * The Google material design color <i>Cyan 600</i>.
     *
     * @since 2.0
     */
    public static final Color CYAN_600 = new Color("#00acc1");

    /**
     * The Google material design color <i>Cyan 700</i>.
     *
     * @since 2.0
     */
    public static final Color CYAN_700 = new Color("#0097a7");

    /**
     * The Google material design color <i>Cyan 800</i>.
     *
     * @since 2.0
     */
    public static final Color CYAN_800 = new Color("#00838f");

    /**
     * The Google material design color <i>Cyan 900</i>.
     *
     * @since 2.0
     */
    public static final Color CYAN_900 = new Color("#006064");

    /**
     * The Google material design color <i>Cyan A100</i>.
     *
     * @since 2.0
     */
    public static final Color CYAN_A100 = new Color("#84ffff");

    /**
     * The Google material design color <i>Cyan A200</i>.
     *
     * @since 2.0
     */
    public static final Color CYAN_A200 = new Color("#18ffff");

    /**
     * The Google material design color <i>Cyan A400</i>.
     *
     * @since 2.0
     */
    public static final Color CYAN_A400 = new Color("#00e5ff");

    /**
     * The Google material design color <i>Cyan A700</i>.
     *
     * @since 2.0
     */
    public static final Color CYAN_A700 = new Color("#00b8d4");

    /**
     * The Google material design color <i>Deep Orange 50</i>.
     *
     * @since 2.1
     */
    public static final Color DEEP_ORANGE_50 = new Color("#fbe9e7");

    /**
     * The Google material design color <i>Deep Orange 100</i>.
     *
     * @since 2.1
     */
    public static final Color DEEP_ORANGE_100 = new Color("#ffccbc");

    /**
     * The Google material design color <i>Deep Orange 200</i>.
     *
     * @since 2.1
     */
    public static final Color DEEP_ORANGE_200 = new Color("#ffab91");

    /**
     * The Google material design color <i>Deep Orange 300</i>.
     *
     * @since 2.1
     */
    public static final Color DEEP_ORANGE_300 = new Color("#ff8a65");

    /**
     * The Google material design color <i>Deep Orange 400</i>.
     *
     * @since 2.1
     */
    public static final Color DEEP_ORANGE_400 = new Color("#ff7043");

    /**
     * The Google material design color <i>Deep Orange 500</i>.
     *
     * @since 2.1
     */
    public static final Color DEEP_ORANGE_500 = new Color("#ff5722");

    /**
     * The Google material design color <i>Deep Orange 600</i>.
     *
     * @since 2.1
     */
    public static final Color DEEP_ORANGE_600 = new Color("#f4511e");

    /**
     * The Google material design color <i>Deep Orange 700</i>.
     *
     * @since 2.1
     */
    public static final Color DEEP_ORANGE_700 = new Color("#e64a19");

    /**
     * The Google material design color <i>Deep Orange 800</i>.
     *
     * @since 2.1
     */
    public static final Color DEEP_ORANGE_800 = new Color("#d84315");

    /**
     * The Google material design color <i>Deep Orange 900</i>.
     *
     * @since 2.1
     */
    public static final Color DEEP_ORANGE_900 = new Color("#bf360c");

    /**
     * The Google material design color <i>Deep Orange A100</i>.
     *
     * @since 2.1
     */
    public static final Color DEEP_ORANGE_A100 = new Color("#ff9e80");

    /**
     * The Google material design color <i>Deep Orange A200</i>.
     *
     * @since 2.1
     */
    public static final Color DEEP_ORANGE_A200 = new Color("#ff6e40");

    /**
     * The Google material design color <i>Deep Orange A400</i>.
     *
     * @since 2.1
     */
    public static final Color DEEP_ORANGE_A400 = new Color("#ff3d00");

    /**
     * The Google material design color <i>Deep Orange A700</i>.
     *
     * @since 2.1
     */
    public static final Color DEEP_ORANGE_A700 = new Color("#dd2c00");

    /**
     * The Google material design color <i>Deep Purple 50</i>.
     *
     * @since 2.1
     */
    public static final Color DEEP_PURPLE_50 = new Color("#ede7f6");

    /**
     * The Google material design color <i>Deep Purple 100</i>.
     *
     * @since 2.1
     */
    public static final Color DEEP_PURPLE_100 = new Color("#d1c4e9");

    /**
     * The Google material design color <i>Deep Purple 200</i>.
     *
     * @since 2.1
     */
    public static final Color DEEP_PURPLE_200 = new Color("#b39ddb");

    /**
     * The Google material design color <i>Deep Purple 300</i>.
     *
     * @since 2.1
     */
    public static final Color DEEP_PURPLE_300 = new Color("#9575cd");

    /**
     * The Google material design color <i>Deep Purple 400</i>.
     *
     * @since 2.1
     */
    public static final Color DEEP_PURPLE_400 = new Color("#7e57c2");

    /**
     * The Google material design color <i>Deep Purple 500</i>.
     *
     * @since 2.1
     */
    public static final Color DEEP_PURPLE_500 = new Color("#673ab7");

    /**
     * The Google material design color <i>Deep Purple 600</i>.
     *
     * @since 2.1
     */
    public static final Color DEEP_PURPLE_600 = new Color("#5e35b1");

    /**
     * The Google material design color <i>Deep Purple 700</i>.
     *
     * @since 2.1
     */
    public static final Color DEEP_PURPLE_700 = new Color("#512da8");

    /**
     * The Google material design color <i>Deep Purple 800</i>.
     *
     * @since 2.1
     */
    public static final Color DEEP_PURPLE_800 = new Color("#4527a0");

    /**
     * The Google material design color <i>Deep Purple 900</i>.
     *
     * @since 2.1
     */
    public static final Color DEEP_PURPLE_900 = new Color("#311b92");

    /**
     * The Google material design color <i>Deep Purple A100</i>.
     *
     * @since 2.1
     */
    public static final Color DEEP_PURPLE_A100 = new Color("#b388ff");

    /**
     * The Google material design color <i>Deep Purple A200</i>.
     *
     * @since 2.1
     */
    public static final Color DEEP_PURPLE_A200 = new Color("#7c4dff");

    /**
     * The Google material design color <i>Deep Purple A400</i>.
     *
     * @since 2.1
     */
    public static final Color DEEP_PURPLE_A400 = new Color("#651fff");

    /**
     * The Google material design color <i>Deep Purple A700</i>.
     *
     * @since 2.1
     */
    public static final Color DEEP_PURPLE_A700 = new Color("#6200ea");

    /**
     * The VGA color <i>fuchsia</i>. Same as <code>new Color(255, 0, 255)</code>.
     *
     * @since 1.0
     */
    public static final Color FUCHSIA = new Color(255, 0, 255);

    /**
     * The VGA color <i>gray</i>. Same as <code>new Color(128, 128, 128)</code>.
     *
     * @since 1.0
     */
    public static final Color GRAY = new Color(128, 128, 128);

    /**
     * The Google material design color <i>Gray 50</i>.
     *
     * @since 2.1
     */
    public static final Color GRAY_50 = new Color("#fafafa");

    /**
     * The Google material design color <i>Gray 100</i>.
     *
     * @since 2.1
     */
    public static final Color GRAY_100 = new Color("#f5f5f5");

    /**
     * The Google material design color <i>Gray 200</i>.
     *
     * @since 2.1
     */
    public static final Color GRAY_200 = new Color("#eeeeee");

    /**
     * The Google material design color <i>Gray 300</i>.
     *
     * @since 2.1
     */
    public static final Color GRAY_300 = new Color("#e0e0e0");

    /**
     * The Google material design color <i>Gray 400</i>.
     *
     * @since 2.1
     */
    public static final Color GRAY_400 = new Color("#bdbdbd");

    /**
     * The Google material design color <i>Gray 500</i>.
     *
     * @since 2.1
     */
    public static final Color GRAY_500 = new Color("#9e9e9e");

    /**
     * The Google material design color <i>Gray 600</i>.
     *
     * @since 2.1
     */
    public static final Color GRAY_600 = new Color("#757575");

    /**
     * The Google material design color <i>Gray 700</i>.
     *
     * @since 2.1
     */
    public static final Color GRAY_700 = new Color("#616161");

    /**
     * The Google material design color <i>Gray 800</i>.
     *
     * @since 2.1
     */
    public static final Color GRAY_800 = new Color("#424242");

    /**
     * The Google material design color <i>Gray 900</i>.
     *
     * @since 2.1
     */
    public static final Color GRAY_900 = new Color("#212121");

    /**
     * The VGA color <i>green</i>. Same as <code>new Color(0, 128, 0)</code>.
     *
     * @since 1.0
     */
    public static final Color GREEN = new Color(0, 128, 0);

    /**
     * The Google material design color <i>Green 50</i>.
     *
     * @since 2.1
     */
    public static final Color GREEN_50 = new Color("#e8f5e9");

    /**
     * The Google material design color <i>Green 100</i>.
     *
     * @since 2.1
     */
    public static final Color GREEN_100 = new Color("#c8e6c9");

    /**
     * The Google material design color <i>Green 200</i>.
     *
     * @since 2.1
     */
    public static final Color GREEN_200 = new Color("#a5d6a7");

    /**
     * The Google material design color <i>Green 300</i>.
     *
     * @since 2.1
     */
    public static final Color GREEN_300 = new Color("#81c784");

    /**
     * The Google material design color <i>Green 400</i>.
     *
     * @since 2.1
     */
    public static final Color GREEN_400 = new Color("#66bb6a");

    /**
     * The Google material design color <i>Green 500</i>.
     *
     * @since 2.1
     */
    public static final Color GREEN_500 = new Color("#4caf50");

    /**
     * The Google material design color <i>Green 600</i>.
     *
     * @since 2.1
     */
    public static final Color GREEN_600 = new Color("#43a047");

    /**
     * The Google material design color <i>Green 700</i>.
     *
     * @since 2.1
     */
    public static final Color GREEN_700 = new Color("#388e3c");

    /**
     * The Google material design color <i>Green 800</i>.
     *
     * @since 2.1
     */
    public static final Color GREEN_800 = new Color("#2e7d32");

    /**
     * The Google material design color <i>Green 900</i>.
     *
     * @since 2.1
     */
    public static final Color GREEN_900 = new Color("#1b5e20");

    /**
     * The Google material design color <i>Green A100</i>.
     *
     * @since 2.1
     */
    public static final Color GREEN_A100 = new Color("#b9f6ca");

    /**
     * The Google material design color <i>Green A200</i>.
     *
     * @since 2.1
     */
    public static final Color GREEN_A200 = new Color("#69f0ae");

    /**
     * The Google material design color <i>Green A400</i>.
     *
     * @since 2.1
     */
    public static final Color GREEN_A400 = new Color("#00e676");

    /**
     * The Google material design color <i>Green A700</i>.
     *
     * @since 2.1
     */
    public static final Color GREEN_A700 = new Color("#00c853");

    /**
     * The Google material design color <i>Indigo 50</i>.
     *
     * @since 2.1
     */
    public static final Color INDIGO_50 = new Color("#e8eaf6");

    /**
     * The Google material design color <i>Indigo 100</i>.
     *
     * @since 2.1
     */
    public static final Color INDIGO_100 = new Color("#c5cae9");

    /**
     * The Google material design color <i>Indigo 200</i>.
     *
     * @since 2.1
     */
    public static final Color INDIGO_200 = new Color("#9fa8da");

    /**
     * The Google material design color <i>Indigo 300</i>.
     *
     * @since 2.1
     */
    public static final Color INDIGO_300 = new Color("#7986cb");

    /**
     * The Google material design color <i>Indigo 400</i>.
     *
     * @since 2.1
     */
    public static final Color INDIGO_400 = new Color("#5c6bc0");

    /**
     * The Google material design color <i>Indigo 500</i>.
     *
     * @since 2.1
     */
    public static final Color INDIGO_500 = new Color("#3f51b5");

    /**
     * The Google material design color <i>Indigo 600</i>.
     *
     * @since 2.1
     */
    public static final Color INDIGO_600 = new Color("#3949ab");

    /**
     * The Google material design color <i>Indigo 700</i>.
     *
     * @since 2.1
     */
    public static final Color INDIGO_700 = new Color("#303f9f");

    /**
     * The Google material design color <i>Indigo 800</i>.
     *
     * @since 2.1
     */
    public static final Color INDIGO_800 = new Color("#283593");

    /**
     * The Google material design color <i>Indigo 900</i>.
     *
     * @since 2.1
     */
    public static final Color INDIGO_900 = new Color("#1a237e");

    /**
     * The Google material design color <i>Indigo A100</i>.
     *
     * @since 2.1
     */
    public static final Color INDIGO_A100 = new Color("#8c9eff");

    /**
     * The Google material design color <i>Indigo A200</i>.
     *
     * @since 2.1
     */
    public static final Color INDIGO_A200 = new Color("#536dfe");

    /**
     * The Google material design color <i>Indigo A400</i>.
     *
     * @since 2.1
     */
    public static final Color INDIGO_A400 = new Color("#3d5afe");

    /**
     * The Google material design color <i>Indigo A700</i>.
     *
     * @since 2.1
     */
    public static final Color INDIGO_A700 = new Color("#304ffe");

    /**
     * The Jeda color. Same as <code>new Color(126, 218, 66)</code>.
     *
     * @since 1.0
     */
    public static final Color JEDA = new Color(126, 218, 66);

    /**
     * The Google material design color <i>Light Blue 50</i>.
     *
     * @since 2.0
     */
    public static final Color LIGHT_BLUE_50 = new Color("#e1f5fe");

    /**
     * The Google material design color <i>Light Blue 100</i>.
     *
     * @since 2.0
     */
    public static final Color LIGHT_BLUE_100 = new Color("#b3e5fc");

    /**
     * The Google material design color <i>Light Blue 200</i>.
     *
     * @since 2.0
     */
    public static final Color LIGHT_BLUE_200 = new Color("#81d4fa");

    /**
     * The Google material design color <i>Light Blue 300</i>.
     *
     * @since 2.0
     */
    public static final Color LIGHT_BLUE_300 = new Color("#4fc3f7");

    /**
     * The Google material design color <i>Light Blue 400</i>.
     *
     * @since 2.0
     */
    public static final Color LIGHT_BLUE_400 = new Color("#29b6f6");

    /**
     * The Google material design color <i>Light Blue 500</i>.
     *
     * @since 2.0
     */
    public static final Color LIGHT_BLUE_500 = new Color("#03a9f4");

    /**
     * The Google material design color <i>Light Blue 600</i>.
     *
     * @since 2.0
     */
    public static final Color LIGHT_BLUE_600 = new Color("#039be5");

    /**
     * The Google material design color <i>Light Blue 700</i>.
     *
     * @since 2.0
     */
    public static final Color LIGHT_BLUE_700 = new Color("#0288d1");

    /**
     * The Google material design color <i>Light Blue 800</i>.
     *
     * @since 2.0
     */
    public static final Color LIGHT_BLUE_800 = new Color("#0277bd");

    /**
     * The Google material design color <i>Light Blue 900</i>.
     *
     * @since 2.0
     */
    public static final Color LIGHT_BLUE_900 = new Color("#01579b");

    /**
     * The Google material design color <i>Light Blue A100</i>.
     *
     * @since 2.0
     */
    public static final Color LIGHT_BLUE_A100 = new Color("#80d8ff");

    /**
     * The Google material design color <i>Light Blue A200</i>.
     *
     * @since 2.0
     */
    public static final Color LIGHT_BLUE_A200 = new Color("#40c4ff");

    /**
     * The Google material design color <i>Light Blue A400</i>.
     *
     * @since 2.0
     */
    public static final Color LIGHT_BLUE_A400 = new Color("#00b0ff");

    /**
     * The Google material design color <i>Light Blue A700</i>.
     *
     * @since 2.0
     */
    public static final Color LIGHT_BLUE_A700 = new Color("#0091ea");

    /**
     * The Google material design color <i>Light Green 50</i>.
     *
     * @since 2.0
     */
    public static final Color LIGHT_GREEN_50 = new Color("#f1f8e9");

    /**
     * The Google material design color <i>Light Green 100</i>.
     *
     * @since 2.0
     */
    public static final Color LIGHT_GREEN_100 = new Color("#dcedc8");

    /**
     * The Google material design color <i>Light Green 200</i>.
     *
     * @since 2.0
     */
    public static final Color LIGHT_GREEN_200 = new Color("#c5e1a5");

    /**
     * The Google material design color <i>Light Green 300</i>.
     *
     * @since 2.0
     */
    public static final Color LIGHT_GREEN_300 = new Color("#aed581");

    /**
     * The Google material design color <i>Light Green 400</i>.
     *
     * @since 2.0
     */
    public static final Color LIGHT_GREEN_400 = new Color("#9ccc65");

    /**
     * The Google material design color <i>Light Green 500</i>.
     *
     * @since 2.0
     */
    public static final Color LIGHT_GREEN_500 = new Color("#8bc34a");

    /**
     * The Google material design color <i>Light Green 600</i>.
     *
     * @since 2.0
     */
    public static final Color LIGHT_GREEN_600 = new Color("#7cb342");

    /**
     * The Google material design color <i>Light Green 700</i>.
     *
     * @since 2.0
     */
    public static final Color LIGHT_GREEN_700 = new Color("#689f38");

    /**
     * The Google material design color <i>Light Green 800</i>.
     *
     * @since 2.0
     */
    public static final Color LIGHT_GREEN_800 = new Color("#558b2f");

    /**
     * The Google material design color <i>Light Green 900</i>.
     *
     * @since 2.0
     */
    public static final Color LIGHT_GREEN_900 = new Color("#33691e");

    /**
     * The Google material design color <i>Light Green A100</i>.
     *
     * @since 2.0
     */
    public static final Color LIGHT_GREEN_A100 = new Color("#ccff90");

    /**
     * The Google material design color <i>Light Green A200</i>.
     *
     * @since 2.0
     */
    public static final Color LIGHT_GREEN_A200 = new Color("#b2ff59");

    /**
     * The Google material design color <i>Light Green A400</i>.
     *
     * @since 2.0
     */
    public static final Color LIGHT_GREEN_A400 = new Color("#76ff03");

    /**
     * The Google material design color <i>Light Green A700</i>.
     *
     * @since 2.0
     */
    public static final Color LIGHT_GREEN_A700 = new Color("#64dd17");

    /**
     * The Google material design color <i>Lime 50</i>.
     *
     * @since 2.0
     */
    public static final Color LIME_50 = new Color("#f9fbe7");

    /**
     * The Google material design color <i>Lime 100</i>.
     *
     * @since 2.0
     */
    public static final Color LIME_100 = new Color("#f0f4c3");

    /**
     * The Google material design color <i>Lime 200</i>.
     *
     * @since 2.0
     */
    public static final Color LIME_200 = new Color("#e6ee9c");

    /**
     * The Google material design color <i>Lime 300</i>.
     *
     * @since 2.0
     */
    public static final Color LIME_300 = new Color("#dce775");

    /**
     * The Google material design color <i>Lime 400</i>.
     *
     * @since 2.0
     */
    public static final Color LIME_400 = new Color("#d4e157");

    /**
     * The Google material design color <i>Lime 500</i>.
     *
     * @since 2.0
     */
    public static final Color LIME_500 = new Color("#cddc39");

    /**
     * The Google material design color <i>Lime 600</i>.
     *
     * @since 2.0
     */
    public static final Color LIME_600 = new Color("#c0ca33");

    /**
     * The Google material design color <i>Lime 700</i>.
     *
     * @since 2.0
     */
    public static final Color LIME_700 = new Color("#afb42b");

    /**
     * The Google material design color <i>Lime 800</i>.
     *
     * @since 2.0
     */
    public static final Color LIME_800 = new Color("#9e9d24");

    /**
     * The Google material design color <i>Lime 900</i>.
     *
     * @since 2.0
     */
    public static final Color LIME_900 = new Color("#827717");

    /**
     * The Google material design color <i>Lime A100</i>.
     *
     * @since 2.0
     */
    public static final Color LIME_A100 = new Color("#f4ff81");

    /**
     * The Google material design color <i>Lime A200</i>.
     *
     * @since 2.0
     */
    public static final Color LIME_A200 = new Color("#eeff41");

    /**
     * The Google material design color <i>Lime A400</i>.
     *
     * @since 2.0
     */
    public static final Color LIME_A400 = new Color("#c6ff00");

    /**
     * The Google material design color <i>Lime A700</i>.
     *
     * @since 2.0
     */
    public static final Color LIME_A700 = new Color("#aeea00");

    /**
     * The VGA color <i>lime</i>. Same as <code>new Color(0, 255, 0)</code>.
     *
     * @since 1.0
     */
    public static final Color LIME = new Color(0, 255, 0);

    /**
     * The VGA color <i>maroon</i>. Same as <code>new Color(128, 0, 0)</code>.
     *
     * @since 1.0
     */
    public static final Color MAROON = new Color(128, 0, 0);

    /**
     * The VGA color <i>navy</i>. Same as <code>new Color(0, 0, 128)</code>.
     *
     * @since 1.0
     */
    public static final Color NAVY = new Color(0, 0, 128);

    /**
     * The VGA color <i>olive</i>. Same as <code>new Color(128, 128, 0)</code>.
     *
     * @since 1.0
     */
    public static final Color OLIVE = new Color(128, 128, 0);

    /**
     * The Google material design color <i>Orange 50</i>.
     *
     * @since 2.1
     */
    public static final Color ORANGE_50 = new Color("#fff3e0");

    /**
     * The Google material design color <i>Orange 100</i>.
     *
     * @since 2.1
     */
    public static final Color ORANGE_100 = new Color("#ffe0b2");

    /**
     * The Google material design color <i>Orange 200</i>.
     *
     * @since 2.1
     */
    public static final Color ORANGE_200 = new Color("#ffcc80");

    /**
     * The Google material design color <i>Orange 300</i>.
     *
     * @since 2.1
     */
    public static final Color ORANGE_300 = new Color("#ffb74d");

    /**
     * The Google material design color <i>Orange 400</i>.
     *
     * @since 2.1
     */
    public static final Color ORANGE_400 = new Color("#ffa726");

    /**
     * The Google material design color <i>Orange 500</i>.
     *
     * @since 2.1
     */
    public static final Color ORANGE_500 = new Color("#ff9800");

    /**
     * The Google material design color <i>Orange 600</i>.
     *
     * @since 2.1
     */
    public static final Color ORANGE_600 = new Color("#fb8c00");

    /**
     * The Google material design color <i>Orange 700</i>.
     *
     * @since 2.1
     */
    public static final Color ORANGE_700 = new Color("#f57c00");

    /**
     * The Google material design color <i>Orange 800</i>.
     *
     * @since 2.1
     */
    public static final Color ORANGE_800 = new Color("#ef6c00");

    /**
     * The Google material design color <i>Orange 900</i>.
     *
     * @since 2.1
     */
    public static final Color ORANGE_900 = new Color("#e65100");

    /**
     * The Google material design color <i>Orange A100</i>.
     *
     * @since 2.1
     */
    public static final Color ORANGE_A100 = new Color("#ffd180");

    /**
     * The Google material design color <i>Orange A200</i>.
     *
     * @since 2.1
     */
    public static final Color ORANGE_A200 = new Color("#ffab40");

    /**
     * The Google material design color <i>Orange A400</i>.
     *
     * @since 2.1
     */
    public static final Color ORANGE_A400 = new Color("#ff9100");

    /**
     * The Google material design color <i>Orange A700</i>.
     *
     * @since 2.1
     */
    public static final Color ORANGE_A700 = new Color("#ff6d00");

    /**
     * The Google material design color <i>Pink 50</i>.
     *
     * @since 2.0
     */
    public static final Color PINK_50 = new Color("#fce4ec");

    /**
     * The Google material design color <i>Pink 100</i>.
     *
     * @since 2.0
     */
    public static final Color PINK_100 = new Color("#f8bbd0");

    /**
     * The Google material design color <i>Pink 200</i>.
     *
     * @since 2.0
     */
    public static final Color PINK_200 = new Color("#f48fb1");

    /**
     * The Google material design color <i>Pink 300</i>.
     *
     * @since 2.0
     */
    public static final Color PINK_300 = new Color("#f06292");

    /**
     * The Google material design color <i>Pink 400</i>.
     *
     * @since 2.0
     */
    public static final Color PINK_400 = new Color("#ec407a");

    /**
     * The Google material design color <i>Pink 500</i>.
     *
     * @since 2.0
     */
    public static final Color PINK_500 = new Color("#e91e63");

    /**
     * The Google material design color <i>Pink 600</i>.
     *
     * @since 2.0
     */
    public static final Color PINK_600 = new Color("#d81b60");

    /**
     * The Google material design color <i>Pink 700</i>.
     *
     * @since 2.0
     */
    public static final Color PINK_700 = new Color("#c2185b");

    /**
     * The Google material design color <i>Pink 800</i>.
     *
     * @since 2.0
     */
    public static final Color PINK_800 = new Color("#ad1457");

    /**
     * The Google material design color <i>Pink 900</i>.
     *
     * @since 2.0
     */
    public static final Color PINK_900 = new Color("#880e4f");

    /**
     * The Google material design color <i>Pink A100</i>.
     *
     * @since 2.0
     */
    public static final Color PINK_A100 = new Color("#ff80ab");

    /**
     * The Google material design color <i>Pink A200</i>.
     *
     * @since 2.0
     */
    public static final Color PINK_A200 = new Color("#ff4081");

    /**
     * The Google material design color <i>Pink A400</i>.
     *
     * @since 2.0
     */
    public static final Color PINK_A400 = new Color("#f50057");

    /**
     * The Google material design color <i>Pink A700</i>.
     *
     * @since 2.0
     */
    public static final Color PINK_A700 = new Color("#c51162");

    /**
     * The VGA color <i>purple</i>. Same as <code>new Color(128, 0, 128)</code>.
     *
     * @since 1.0
     */
    public static final Color PURPLE = new Color(128, 0, 128);

    /**
     * The Google material design color <i>Purple 50</i>.
     *
     * @since 2.1
     */
    public static final Color PURPLE_50 = new Color("#f3e5f5");

    /**
     * The Google material design color <i>Purple 100</i>.
     *
     * @since 2.1
     */
    public static final Color PURPLE_100 = new Color("#e1bee7");

    /**
     * The Google material design color <i>Purple 200</i>.
     *
     * @since 2.1
     */
    public static final Color PURPLE_200 = new Color("#ce93d8");

    /**
     * The Google material design color <i>Purple 300</i>.
     *
     * @since 2.1
     */
    public static final Color PURPLE_300 = new Color("#ba68c8");

    /**
     * The Google material design color <i>Purple 400</i>.
     *
     * @since 2.1
     */
    public static final Color PURPLE_400 = new Color("#ab47bc");

    /**
     * The Google material design color <i>Purple 500</i>.
     *
     * @since 2.1
     */
    public static final Color PURPLE_500 = new Color("#9c27b0");

    /**
     * The Google material design color <i>Purple 600</i>.
     *
     * @since 2.1
     */
    public static final Color PURPLE_600 = new Color("#8e24aa");

    /**
     * The Google material design color <i>Purple 700</i>.
     *
     * @since 2.1
     */
    public static final Color PURPLE_700 = new Color("#7b1fa2");

    /**
     * The Google material design color <i>Purple 800</i>.
     *
     * @since 2.1
     */
    public static final Color PURPLE_800 = new Color("#6a1b9a");

    /**
     * The Google material design color <i>Purple 900</i>.
     *
     * @since 2.1
     */
    public static final Color PURPLE_900 = new Color("#4a148c");

    /**
     * The Google material design color <i>Purple A100</i>.
     *
     * @since 2.1
     */
    public static final Color PURPLE_A100 = new Color("#ea80fc");

    /**
     * The Google material design color <i>Purple A200</i>.
     *
     * @since 2.1
     */
    public static final Color PURPLE_A200 = new Color("#e040fb");

    /**
     * The Google material design color <i>Purple A400</i>.
     *
     * @since 2.1
     */
    public static final Color PURPLE_A400 = new Color("#d500f9");

    /**
     * The Google material design color <i>Purple A700</i>.
     *
     * @since 2.1
     */
    public static final Color PURPLE_A700 = new Color("#aa00ff");

    /**
     * The VGA color <i>red</i>. Same as <code>new Color(255, 0, 0)</code>.
     *
     * @since 1.0
     */
    public static final Color RED = new Color(255, 0, 0);

    /**
     * The Google material design color <i>Red 50</i>.
     *
     * @since 2.0
     */
    public static final Color RED_50 = new Color("#ffebee");

    /**
     * The Google material design color <i>Red 100</i>.
     *
     * @since 2.0
     */
    public static final Color RED_100 = new Color("#ffcdd2");

    /**
     * The Google material design color <i>Red 200</i>.
     *
     * @since 2.0
     */
    public static final Color RED_200 = new Color("#ef9a9a");

    /**
     * The Google material design color <i>Red 300</i>.
     *
     * @since 2.0
     */
    public static final Color RED_300 = new Color("#e57373");

    /**
     * The Google material design color <i>Red 400</i>.
     *
     * @since 2.0
     */
    public static final Color RED_400 = new Color("#ef5350");

    /**
     * The Google material design color <i>Red 500</i>.
     *
     * @since 2.0
     */
    public static final Color RED_500 = new Color("#f44336");

    /**
     * The Google material design color <i>Red 600</i>.
     *
     * @since 2.0
     */
    public static final Color RED_600 = new Color("#e53935");

    /**
     * The Google material design color <i>Red 700</i>.
     *
     * @since 2.0
     */
    public static final Color RED_700 = new Color("#d32f2f");

    /**
     * The Google material design color <i>Red 800</i>.
     *
     * @since 2.0
     */
    public static final Color RED_800 = new Color("#c62828");

    /**
     * The Google material design color <i>Red 900</i>.
     *
     * @since 2.0
     */
    public static final Color RED_900 = new Color("#b71c1c");

    /**
     * The Google material design color <i>Red A100</i>.
     *
     * @since 2.0
     */
    public static final Color RED_A100 = new Color("#ff8a80");

    /**
     * The Google material design color <i>Red A200</i>.
     *
     * @since 2.0
     */
    public static final Color RED_A200 = new Color("#ff5252");

    /**
     * The Google material design color <i>Red A400</i>.
     *
     * @since 2.0
     */
    public static final Color RED_A400 = new Color("#ff1744");

    /**
     * The Google material design color <i>Red A700</i>.
     *
     * @since 2.0
     */
    public static final Color RED_A700 = new Color("#d50000");

    /**
     * The VGA color <i>silver</i>. Same as <code>new Color(192, 192, 192)</code>.
     *
     * @since 1.0
     */
    public static final Color SILVER = new Color(192, 192, 192);

    /**
     * The VGA color <i>teal</i>. Same as <code>new Color(0, 128, 128)</code>.
     *
     * @since 1.0
     */
    public static final Color TEAL = new Color(0, 128, 128);

    /**
     * The Google material design color <i>Teal 50</i>.
     *
     * @since 2.1
     */
    public static final Color TEAL_50 = new Color("#e0f2f1");

    /**
     * The Google material design color <i>Teal 100</i>.
     *
     * @since 2.1
     */
    public static final Color TEAL_100 = new Color("#b2dfdb");

    /**
     * The Google material design color <i>Teal 200</i>.
     *
     * @since 2.1
     */
    public static final Color TEAL_200 = new Color("#80cbc4");

    /**
     * The Google material design color <i>Teal 300</i>.
     *
     * @since 2.1
     */
    public static final Color TEAL_300 = new Color("#4db6ac");

    /**
     * The Google material design color <i>Teal 400</i>.
     *
     * @since 2.1
     */
    public static final Color TEAL_400 = new Color("#26a69a");

    /**
     * The Google material design color <i>Teal 500</i>.
     *
     * @since 2.1
     */
    public static final Color TEAL_500 = new Color("#009688");

    /**
     * The Google material design color <i>Teal 600</i>.
     *
     * @since 2.1
     */
    public static final Color TEAL_600 = new Color("#00897b");

    /**
     * The Google material design color <i>Teal 700</i>.
     *
     * @since 2.1
     */
    public static final Color TEAL_700 = new Color("#00796b");

    /**
     * The Google material design color <i>Teal 800</i>.
     *
     * @since 2.1
     */
    public static final Color TEAL_800 = new Color("#00695c");

    /**
     * The Google material design color <i>Teal 900</i>.
     *
     * @since 2.1
     */
    public static final Color TEAL_900 = new Color("#004d40");

    /**
     * The Google material design color <i>Teal A100</i>.
     *
     * @since 2.1
     */
    public static final Color TEAL_A100 = new Color("#a7ffeb");

    /**
     * The Google material design color <i>Teal A200</i>.
     *
     * @since 2.1
     */
    public static final Color TEAL_A200 = new Color("#64ffda");

    /**
     * The Google material design color <i>Teal A400</i>.
     *
     * @since 2.1
     */
    public static final Color TEAL_A400 = new Color("#1de9b6");

    /**
     * The Google material design color <i>Teal A700</i>.
     *
     * @since 2.1
     */
    public static final Color TEAL_A700 = new Color("#00bfa5");

    /**
     * The fully transparent color.
     *
     * @since 1.0
     */
    public static final Color TRANSPARENT = new Color(255, 255, 255, 0);

    /**
     * The VGA color <i>white</i>. Same as <code>new Color(255, 255, 255)</code>.
     *
     * @since 1.0
     */
    public static final Color WHITE = new Color(255, 255, 255);

    /**
     * The VGA color <i>yellow</i>. Same as <code>new Color(255, 255, 0)</code>.
     *
     * @since 1.0
     */
    public static final Color YELLOW = new Color(255, 255, 0);

    /**
     * The Google material design color <i>Yellow 50</i>.
     *
     * @since 2.1
     */
    public static final Color YELLOW_50 = new Color("#fffde7");

    /**
     * The Google material design color <i>Yellow 100</i>.
     *
     * @since 2.1
     */
    public static final Color YELLOW_100 = new Color("#fff9c4");

    /**
     * The Google material design color <i>Yellow 200</i>.
     *
     * @since 2.1
     */
    public static final Color YELLOW_200 = new Color("#fff59d");

    /**
     * The Google material design color <i>Yellow 300</i>.
     *
     * @since 2.1
     */
    public static final Color YELLOW_300 = new Color("#fff176");

    /**
     * The Google material design color <i>Yellow 400</i>.
     *
     * @since 2.1
     */
    public static final Color YELLOW_400 = new Color("#ffee58");

    /**
     * The Google material design color <i>Yellow 500</i>.
     *
     * @since 2.1
     */
    public static final Color YELLOW_500 = new Color("#ffeb3b");

    /**
     * The Google material design color <i>Yellow 600</i>.
     *
     * @since 2.1
     */
    public static final Color YELLOW_600 = new Color("#fdd835");

    /**
     * The Google material design color <i>Yellow 700</i>.
     *
     * @since 2.1
     */
    public static final Color YELLOW_700 = new Color("#fbc02d");

    /**
     * The Google material design color <i>Yellow 800</i>.
     *
     * @since 2.1
     */
    public static final Color YELLOW_800 = new Color("#f9a825");

    /**
     * The Google material design color <i>Yellow 900</i>.
     *
     * @since 2.1
     */
    public static final Color YELLOW_900 = new Color("#f57f17");

    /**
     * The Google material design color <i>Yellow A100</i>.
     *
     * @since 2.1
     */
    public static final Color YELLOW_A100 = new Color("#ffff8d");

    /**
     * The Google material design color <i>Yellow A200</i>.
     *
     * @since 2.1
     */
    public static final Color YELLOW_A200 = new Color("#ffff00");

    /**
     * The Google material design color <i>Yellow A400</i>.
     *
     * @since 2.1
     */
    public static final Color YELLOW_A400 = new Color("#ffea00");

    /**
     * The Google material design color <i>Yellow A700</i>.
     *
     * @since 2.1
     */
    public static final Color YELLOW_A700 = new Color("#ffd600");

    private final int value;

    /**
     * Constructs a color from an internal value.
     *
     * @param value the internal value
     *
     * @since 1.0
     */
    public Color(final int value) {
        this.value = value;
    }

    /**
     * Constructs a color from serialized data.
     *
     * @param data the serialized data
     *
     * @since 1.2
     */
    public Color(final Data data) {
        this(data.readInt(R), data.readInt(G), data.readInt(B), data.readInt(A));
    }

    /**
     * Constructs an RGB color. The values for the red, green, and blue components of the color can be specified. All
     * values must be in the range from 0 to 255. Fractions are rounded down, values smaller than 0 are interpreted as
     * 0, values greater than 255 are interpreted as 255.
     *
     * @param red the red component
     * @param green the green component
     * @param blue the blue component
     *
     * @since 2.3
     */
    public Color(final double red, final double green, final double blue) {
        this((int) red, (int) green, (int) blue, 255);
    }

    /**
     * Constructs an RGB color. The values for the red, green, and blue components of the color can be specified. All
     * values must be in the range from 0 to 255. Values smaller than 0 are interpreted as 0, values greater than 255
     * are interpreted as 255.
     *
     * @param red the red component
     * @param green the green component
     * @param blue the blue component
     *
     * @since 1.0
     */
    public Color(final int red, final int green, final int blue) {
        this(red, green, blue, 255);
    }

    /**
     * Constructs an RGBA color. The values for the red, green, blue, and alpha components of the color can be
     * specified. All values must be in the range from 0 to 255. Values smaller than 0 are interpreted as 0, values
     * greater than 255 are interpreted as 255.
     *
     * @param red the red component
     * @param green the green component
     * @param blue the blue component
     * @param alpha the opacity
     *
     * @since 2.3
     */
    public Color(final double red, final double green, final double blue, final double alpha) {
        this((int) red, (int) green, (int) blue, (int) alpha);
    }

    /**
     * Constructs an RGBA color. The values for the red, green, blue, and alpha components of the color can be
     * specified. All values must be in the range from 0 to 255. Values smaller than 0 are interpreted as 0, values
     * greater than 255 are interpreted as 255.
     *
     * @param red the red component
     * @param green the green component
     * @param blue the blue component
     * @param alpha the opacity
     *
     * @since 1.0
     */
    public Color(final int red, final int green, final int blue, final int alpha) {
        value = (toRange(alpha) << 24) | (toRange(red) << 16) | (toRange(green) << 8) | toRange(blue);
    }

    /**
     * Constructs a color from an HTML color specification. The string must contain an HTML color that starts with an
     * '#', followed by three two-digit hex values for red, green, and blue (e.g. <b>#ABCDEF</b>).
     *
     * @param value the HTML color specification
     * @throws NullPointerException if <code>value</code> is <code>null</code>
     * @throws IllegalArgumentException if <code>value</code> does not contain a valid HTML color
     *
     * @since 1.0
     */
    public Color(final String value) {
        if (value == null) {
            throw new NullPointerException("value");
        }

        if (value.length() != 7) {
            throw new IllegalArgumentException("'" + value + "' is not a valid HTML color, it's length is not 7.");
        }

        if (value.charAt(0) != '#') {
            throw new IllegalArgumentException("'" + value + "' is not a valid HTML color, it doesn't start with #.");
        }

        final int red = Integer.parseInt(value.substring(1, 3), 16);
        final int green = Integer.parseInt(value.substring(3, 5), 16);
        final int blue = Integer.parseInt(value.substring(5, 7), 16);
        this.value = (255 << 24) | (red << 16) | (green << 8) | blue;
    }

    @Override
    public boolean equals(final Object object) {
        if (object instanceof Color) {
            final Color other = (Color) object;
            return value == other.value;
        }
        else {
            return false;
        }
    }

    /**
     * Returns the alpha component of this color.
     *
     * @return the alpha component of this color
     *
     * @since 1.0
     */
    public int getAlpha() {
        return 255 & (value >> 24);
    }

    /**
     * Returns the blue component of this color.
     *
     * @return the blue component of this color
     *
     * @since 1.0
     */
    public int getBlue() {
        return 255 & value;
    }

    /**
     * Returns the green component of this color.
     *
     * @return the green component of this color
     *
     * @since 1.0
     */
    public int getGreen() {
        return 255 & (value >> 8);
    }

    /**
     * Returns the red component of this color.
     *
     * @return the red component of this color
     *
     * @since 1.0
     */
    public int getRed() {
        return 255 & (value >> 16);
    }

    /**
     * The internal value of this color.
     *
     * @since 1.0
     */
    public int getValue() {
        return value;
    }

    @Override
    public int hashCode() {
        return 23 * value;
    }

    /**
     * Returns a text representation of this color. The returned text is a CSS 3 color specification of this color. It
     * has the form <code>"rgb(R, G, B)"</code> or <code>"rgba(R, G, B, A)"</code> where R, G, and B are the red, green,
     * and blue components ranging from 0 to 255 and A ist the alpha component ranging from 0 to 1.
     *
     * @return CSS 3 color specification of the color
     */
    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder();
        final int alpha = getAlpha();
        if (alpha != 255) {
            result.append("rgba(");
        }
        else {
            result.append("rgb(");
        }

        result.append(getRed());
        result.append(", ");
        result.append(getGreen());
        result.append(", ");
        result.append(getBlue());
        if (alpha != 255) {
            result.append(", ");
            result.append(alpha / 255.0);
        }

        result.append(")");
        return result.toString();
    }

    @Override
    public void writeTo(final Data data) {
        data.writeInt(R, getRed());
        data.writeInt(G, getGreen());
        data.writeInt(B, getBlue());
        data.writeInt(A, getAlpha());
    }

    private static int toRange(final int value) {
        return Math.max(0, Math.min(value, 255));
    }
}
