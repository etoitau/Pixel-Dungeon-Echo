/*
 * Pixel Dungeon Echo
 * Copyright (C) 2019 Kyle Chatman
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.etoitau.pixeldungeon;

import com.etoitau.pixeldungeon.items.armor.Armor;
import com.etoitau.pixeldungeon.items.armor.glyphs.AutoRepair;
import com.etoitau.pixeldungeon.items.armor.glyphs.Metabolism;
import com.etoitau.pixeldungeon.items.weapon.Weapon;
import com.etoitau.pixeldungeon.items.weapon.enchantments.Instability;
import com.etoitau.pixeldungeon.items.weapon.enchantments.Tempering;

import java.util.HashMap;
import java.util.Map;

public class DegradationFilter {

    private static Map<Object, Object> filterMap = new HashMap<>();
//    private static Map<String, String> stringFilter = new HashMap<>();
//    private static Map<Class<Weapon.Enchantment>, Class<Weapon.Enchantment>> enchantmentFilter = new HashMap<>();
//    private static Map<Class<Armor.Glyph>, Class<Armor.Glyph>> glyphFilter = new HashMap<>();

    static {
        // enchantmentFilter.put((Weapon.Enchantment) Tempering.class, Instability.class);
        filterMap.put(Tempering.class, Instability.class);
        filterMap.put(AutoRepair.class, Metabolism.class);
    }

    private static DegradationFilter instance;

    private DegradationFilter() {}

    public static DegradationFilter get() {
        if (instance == null) {
            instance = new DegradationFilter();
        }
        return instance;
    }

    public static DegradationFilter add(String toReplace, String withThis) {
        filterMap.put(toReplace, withThis);
        //stringFilter.put(toReplace, withThis);
        return get();
    }

    // sign or other text
    public static String filterString(String stringToCheck) {
        if (PixelDungeon.itemDeg()) {
            String found = (String) filterMap.get(stringToCheck);
            return (found == null)? stringToCheck: found;
        } else {
            return stringToCheck;
        }
    }

    // weapon enchantment
    public static Class<Weapon.Enchantment> filterEnchantment(Class<Weapon.Enchantment> enchToCheck) {
        if (PixelDungeon.itemDeg()) {
            Class<Weapon.Enchantment> found = (Class<Weapon.Enchantment>) filterMap.get(enchToCheck);
            return (found == null)? enchToCheck: found;
        } else {
            return enchToCheck;
        }
    }

    // armor enchantment
    public static Class<Armor.Glyph> filterGlyph(Class<Armor.Glyph> glyphToCheck) {
        if (PixelDungeon.itemDeg()) {
            Class<Armor.Glyph> found = (Class<Armor.Glyph>) filterMap.get(glyphToCheck);
            return (found == null)? glyphToCheck: found;
        } else {
            return glyphToCheck;
        }
    }


}
