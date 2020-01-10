# Pixel Dungeon Echo
A modification of Pixel Dungeon version 1.9.2a by [watabou](https://github.com/watabou/pixel-dungeon) (Vanilla)
including significant content from Skillful Pixel Dungeon version 0.4.5 by [bilbolPrime](https://github.com/bilbolPrime/SPD) (SPD). 
 
Thanks also to [mdsimmo](https://github.com/mdsimmo/pixel-dungeon) for setting up a build-friendly version

And to [Nathan Wolfe](https://github.com/nathbenjwolf) for the Potion Belt pull request on Vanilla which was useful in adding it here.

## License
Developed by [Kyle Chatman](http://www.kchatman.com) under a GNU Public License. See LICENSE.txt

## YouTube
[https://youtu.be/pNctc8LfpTc](https://youtu.be/pNctc8LfpTc)

## Google Play
[https://play.google.com/store/apps/details?id=com.etoitau.pixeldungeon](https://play.google.com/store/apps/details?id=com.etoitau.pixeldungeon)

## Description of Changes
### v. 1.1.1
* UI 
    * Added subscript icons for identified wands, rings, potions, scrolls
* Feature
    * A bonus skill can be unlocked for each class by discovering an in-game secret
* Bug fixes
    * Iron tip skill being applied to any item
* Misc code refactoring/cleanup

### v. 1.1.0
* Feature - Major change to how Ankhs work
    * You can now use an ankh to travel back in time a short distance. On use, ankh degrades into a 
    cracked ankh which can be used to time travel one more time.
    * Both regular and cracked ankh can resurrect from dead, and inventory is not emptied.
    * Resurrecting from a cracked ankh has some side effects. 
* Tweaks and balancing
    * Turned down mob death screams, added some new screams
    * Nerfed Huntress Hunting skill
    * Potion of Experience can overflow to next level 
* Bug fixes
    * Necroblade message even when full
    * Ring addition - Two rings of same type weren't additive for some types (herbalism, elements, power)
    * If degradation turned off, still get degradation-related enchantments and signs
    * Double Strike double shout
    * Graphical artifact in backpack when degradation turned off
* Misc grammar/wording changes
* Misc code refactoring/cleanup    
### v. 1.0.5
* Bug fixes
    * Ankh not wiping out backpack
### v. 1.0.4
* Reworked signpost system
    * New tips
    * Random set of tips each playthrough
* Dew now only collected if injured or space in dew vial
* Bug fixes
    * "Can't advance skill" window not dismissing correctly
    * Awareness skill not working
    * Some skills shouting without any upgrades
    * Wands not recharging after resurrection
* Misc grammar/wording changes
* Misc code refactoring/cleanup
### v. 1.0.3
* Mob health indicator reworked so more than one mob can show one at a time
* Sacrificial Fire rework
    * Mark is spread by violence
    * Standing in fire is like using Scroll of Challenge
    * Mob spawn rate increased (like night time)
    * Usable, but more grinding required if xp level too high
* Graphical changes to skill tree and skill quickslot
* Bug fixes
    * key count did not include boss skull keys 
    * Rogue Venom skill not working
    * Quickslot confused by multiple types of arrows
    * Storage chest could spawn overlapping wandmaker
* Added debugging features
    * Better error reporting window
    * "God mode"
* Misc grammar/wording changes
* Misc code refactoring/cleanup
### v. 1.0.2
* Nerfed necroblade
* Bug fixes
    * Iron tip only shouts if actually used
* Misc grammar/wording changes
* Misc code refactoring/cleanup
### v. 1.0.1 (vs. Skillful Pixel Dungeon base code)
* Added option for turning off permadeath
    * Similar to Ankh in vanilla, but items are retained
* Difficulty rework
    * Hunger reduction (scaled by difficulty)
    * Changed secret discovery bonus to scale with difficulty
    * Added very easy mode, changed hell mode to hard
    * Length of night scales with difficulty
    * Removed difficulty prefix for mobs 
* Night now based on in-game time instead of devices' clock
* Added potion belt
* Keys don't require keyring or space in backpack
* Iron Tip skill reworked to allow user to aim at front mob
* Preference defaults
    * Sound/music off
    * Second quickslot on
    * No degredation  
* Removed Skillful PD features
    * In-game mob difficulty customization
    * Hatsune/Legend missions
    * Mercenaries
* Bug fixes
    * Skill bonus at high difficulty
    * Storage chest doesn't work at last three bosses
    * Skills not working: Hunting, Aimed Shot
* Misc balance tweaks
* Misc grammar/wording changes
* Misc code refactoring/cleanup
