package com.ghstudios.android.data.classes

import android.util.Log

import java.util.ArrayList
import java.util.Arrays



/*
 * Class for Weapon
 */
class Weapon : Item() {
    companion object {
        /* Constant weapon types */
        const val GREAT_SWORD = "Great Sword"
        const val LONG_SWORD = "Long Sword"
        const val SWORD_AND_SHIELD = "Sword and Shield"
        const val DUAL_BLADES = "Dual Blades"
        const val HAMMER = "Hammer"
        const val HUNTING_HORN = "Hunting Horn"
        const val LANCE = "Lance"
        const val GUN_LANCE = "Gunlance"
        const val SWITCH_AXE = "Switch Axe"
        const val CHARGE_BLADE = "Charge Blade"
        const val INSECT_GLAIVE = "Insect Glaive"
        const val LIGHT_BOWGUN = "Light Bowgun"
        const val HEAVY_BOWGUN = "Heavy Bowgun"
        const val BOW = "Bow"
    }

    // Weapon type
    var wtype: String? = ""

    var creationCost = -1
    var upgradeCost = -1
    var attack = -1
    var maxAttack = -1

    // Elemental attack type
    var element: String? = ""

    // Second element type
    var element2: String? = ""

    // Awakened elemental type
    var awaken: String? = ""

    val elementEnum get() = getElementFromString(element)
    val element2Enum get() = getElementFromString(element2)
    val awakenElementEnum get() = getElementFromString(awaken)

    var elementAttack: Long = 0
    var element2Attack: Long = 0
    var awakenAttack: Long = 0

    // Defense given by the weapon
    var defense = -1

    // Sharpness values
    var sharpness: String? = ""

    // Affinity
    var affinity: String? = ""

    // Horn notes
    var hornNotes: String? = ""

    // Shelling type
    var shellingType: String? = ""

    // Phial type
    var phial: String? = ""

    // Charges for bows
    var charges = ""
        set(charges) {
            field = charges
            var charge = ""
            val level = ""

            val new_charges = charges.split("\\|".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            for (c in new_charges) {

                if (c.endsWith("*")) {
                    charge = "(" + c.substring(0, c.length - 3) + c[c.length - 2] + ")"
                } else {
                    charge = c.substring(0, c.length - 2) + c[c.length - 1]
                }

                this.chargeString = this.chargeString + charge + " / "
            }
            chargeString = chargeString.substring(0, this.chargeString.length - 3)
        }

    // Coatings for bows
    var coatings: String? = ""
        set(coatings) {
            field = coatings

            this.coatingsArray = coatings?.split("\\|".toRegex())?.dropLastWhile { it.isEmpty() }?.toTypedArray()
        }

    var recoil: String? = ""                    // Recoils for bowguns; arc for bows
    var reloadSpeed: String? = ""               // Reload speed for bowguns
    var rapidFire: String? = ""                 // Rapid fire/crouching fire for bowguns
    var deviation: String? = ""                 // Deviation for bowguns
    var ammo: String? = ""                      // Ammo for bowguns
    var specialAmmo: String? = ""               // Special ammo for bowguns (rapid fire ammo?)

    // Set the slot to view
    // Unicode White Circle \u25CB
    // Unicode FIGURE DASH \u2012
    var numSlots = -1

    // Final in weapon tree or not
    var wFinal = -1

    // Depth of weapon in weapon tree
    var tree_Depth = 0

    var parentId: Int = 0

    val slotString get() = when (this.numSlots) {
        0 -> "\u2012\u2012\u2012"
        1 -> "\u25CB\u2012\u2012"
        2 -> "\u25CB\u25CB\u2012"
        3 -> "\u25CB\u25CB\u25CB"
        else -> "error!!"
    }

    var sharpness1: IntArray? = null
        private set
    var sharpness2: IntArray? = null
        private set
    var sharpness3: IntArray? = null
        private set
    var coatingsArray: Array<String>? = null
        private set
    var chargeString = ""
        private set

    val attackString: String
        get() = Integer.toString(attack)

    fun initializeSharpness() {
        // Sharpness is in the format "1.1.1.1.1.1.1 1.1.1.1.1.1.1" where each
        // 1 is an int representing the sharpness value of a certain color.
        // The order is red, orange, yellow, green, white, purple.
        // First set is for regular sharpness, second set is for sharpness+1

        // early exit if sharpness is null
        val sharpness = this.sharpness ?: return

        //Joe: For MHX, there is only 6 levels of sharpness, but the extra doesn't hurt anything.
        var sharpness1 = IntArray(7)
        var sharpness2 = IntArray(7)
        var sharpness3 = IntArray(7)

        //separate both sets of sharpness
        val strSharpnessBoth = sharpness.split(" ".toRegex()).dropLastWhile { it.isEmpty() }

        //convert sharpness strings to arrays
        val strSharpness1 = strSharpnessBoth[0].split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toMutableList()
        val strSharpness2 = strSharpnessBoth[1].split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toMutableList()
        val strSharpness3 = strSharpnessBoth[2].split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toMutableList()

        //add leading 0s to those with less than purple sharpness
        while (strSharpness1.size <= 7) {
            strSharpness1.add("0")
        }
        while (strSharpness2.size <= 7) {
            strSharpness2.add("0")
        }
        while (strSharpness3.size <= 7) {
            strSharpness3.add("0")
        }

        // Error handling logs error and passes empty sharpness bars
        for (i in 0..6) {
            try {
                sharpness1[i] = Integer.parseInt(strSharpness1[i])
            } catch (e: Exception) {
                Log.v("ParseSharpness", "Error in sharpness $sharpness")
                sharpness1 = intArrayOf(0, 0, 0, 0, 0, 0, 0)
                break
            }
        }
        for (i in 0..6) {
            try {
                sharpness2[i] = Integer.parseInt(strSharpness2[i])
            } catch (e: Exception) {
                Log.v("ParseSharpness", "Error in sharpness $sharpness")
                sharpness2 = intArrayOf(0, 0, 0, 0, 0, 0, 0)
                break
            }
        }

        for (i in 0..6) {
            try {
                sharpness3[i] = Integer.parseInt(strSharpness3[i])
            } catch (e: Exception) {
                Log.v("ParseSharpness", "Error in sharpness $sharpness")
                sharpness3 = intArrayOf(0, 0, 0, 0, 0, 0, 0)
                break
            }
        }

        this.sharpness1 = sharpness1
        this.sharpness2 = sharpness2
        this.sharpness3 = sharpness3
    }
}
