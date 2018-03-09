Pixel Dungeon
=============

This is a fork of Watabou's Pixel Dungeon. This repo adds no new features,
but aims to make setting up Pixel Dungeon a much easier process which everyone
can easilly build off.

To setup Pixel Dungeon:

1. Fork this repo
2. Clone the forked repo (note the "--recursive" part): `git clone --recursive https://github.com/your-name/pixel-dungeon`
3. Start Intellij/Android Studio
4. Go to File->Open and select the cloned repo
5. You're done :)

Note that the PD-classes library is included as a submodule in the 'game-engine' directory. 
If you want to make changes to that library, you'll need to fork the PD-classes library
found here: https://github.com/mdsimmo/PD-classes

## What's different?

Primarilly, this repo has all the gradle build files included so you can easilly start using
the project with an IDE.

This project also fixes several small compile issues and several oddities in the code.

## Common prolems

### Opening the project in Android Studio results in "Configuration with name 'default' not found"
That's probably because you cloned the repo without the '--recursive' part. To fix it, run the 
following from the projects root directory:
```
git submodule init
git submodule update
cd game-engine
git checkout master
```
