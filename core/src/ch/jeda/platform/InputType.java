/*
 * Copyright (C) 2012 - 2013 by Stefan Rothe
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

import java.util.HashMap;
import java.util.Map;

/**
 * <b>Internal</b>. Do not use this class.
 */
public abstract class InputType<T> {

    private static Map<Class<?>, InputType<?>> MAP = initMap();

    @SuppressWarnings("unchecked")
    public static <T> InputType<T> forClass(final Class<T> clazz) {
        return (InputType<T>) MAP.get(clazz);
    }

    private InputType() {
    }

    public abstract T parse(String text);

    public abstract boolean validate(String text);

    private static Map<Class<?>, InputType<?>> initMap() {
        final Map<Class<?>, InputType<?>> result = new HashMap<Class<?>, InputType<?>>();
        result.put(Double.class, new DoubleInputType());
        result.put(Integer.class, new IntInputType());
        result.put(String.class, new StringInputType());
        return result;
    }

    private static class DoubleInputType extends InputType<Double> {

        @Override
        public Double parse(final String text) {
            try {
                return Double.parseDouble(text);
            }
            catch (NumberFormatException ex) {
                return null;
            }
        }

        @Override
        public boolean validate(final String text) {
            return this.parse(text) != null;
        }
    }

    private static class IntInputType extends InputType<Integer> {

        @Override
        public Integer parse(final String text) {
            try {
                return Integer.parseInt(text);
            }
            catch (NumberFormatException ex) {
                return null;
            }
        }

        @Override
        public boolean validate(final String text) {
            return this.parse(text) != null;
        }
    }

    private static class StringInputType extends InputType<String> {

        @Override
        public String parse(final String text) {
            return text;
        }

        @Override
        public boolean validate(final String text) {
            return true;
        }
    }
}
