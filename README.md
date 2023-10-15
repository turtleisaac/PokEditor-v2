# PokEditor v2

**Author: Turtleisaac**

# Development of PokEditor v2 has ended and will be replaced with [PokEditor v3](https://github.com/turtleisaac/PokEditor). However, v3 is not ready, so continue using v2 in the meantime.

Multifunctional in-depth editor for Pokémon Gen IV (4) game data.

Written entirely in Java and is completely OS-agnostic. 

**Java 11 is required.** 12 or higher may work, but if older classes have been deprecated or removed in those versions, then it may not work for those higher versions of Java.

This tool is still in active development and will receive periodic updates as improvements are made and bugs are found, time permitting.

**Note:** Works best on UNIX-based systems such as macOS or Linux

Special thanks to [JackHack96](https://github.com/JackHack96), [Jay-San](https://www.youtube.com/channel/UCGs237E1PfrfIGsZ9eqF-lw), [Drayano](https://twitter.com/Drayano60), [Vendor](https://twitter.com/VendorPC), FrankieD, [Mikelan98](https://twitter.com/mikelan98?lang=en), and [HelloOO7](https://github.com/HelloOO7)

Please join [this Discord server](https://discord.gg/zAtqJDW2jC) for help with using PokEditor v2 or for help with any questions relating to Pokémon Gen 4 and 5 hacking.

The template sheets can be found [here](https://drive.google.com/drive/folders/1hlKiP7V31Ddj4WmKnjK7lfhT88yPjB55?usp=sharing).

**Note:** Code from the [Universal Pokemon Randomizer ZX](https://github.com/Ajarmar/universal-pokemon-randomizer-zx) is adapted for LZ decompression purposes under the rights provided by the GNU General Public License v3.0. If there are any complaints related to this, please create a new Issue in the Issues tab here on GitHub.

![PokEditor Personal Editor](https://i.imgur.com/YyBOyCY.png)
![image](https://user-images.githubusercontent.com/7987859/110886228-5401e080-82b6-11eb-8bea-40a5dfaa8120.png)
![image](https://user-images.githubusercontent.com/7987859/110886253-5b28ee80-82b6-11eb-92e0-ef36e24cece4.png)
![image](https://user-images.githubusercontent.com/7987859/110886289-6aa83780-82b6-11eb-86f7-d5867584b841.png)
![image](https://user-images.githubusercontent.com/7987859/110886399-8f9caa80-82b6-11eb-991a-b052f54c1cc3.png)
![image](https://user-images.githubusercontent.com/7987859/110886318-73990900-82b6-11eb-8f81-8a17c37ee2e4.png)

# Usage

Open a Terminal (macOS/Linux) or Command Prompt (Windows) window, navigate to the same folder that your downloaded PokEditor v2 JAR file is in, and run:```java -Dfile.encoding=UTF-8 -jar PokEditor-v2.jar```

**Note:** If you are on macOS or Linux, you can just double-click the JAR instead, but it is recommended to run from Terminal so you can monitor the cmd output and report errors more easily.

# List of Spreadsheet-Based Editors

* Personal Data Editor (stats, types, abilities, etc...)

* TM Learnset Editor

* Level-Up Learnset Editor

* Encounter Editor (also has a GUI-based editor) (currently incomplete)

* Evolutions Editor

* Item Editor

* Move Editor

* Move Tutor Editor (moves taught and compatibility)

* Baby Form Editor (what hatches from an egg)

* Trainer Editor (also has a GUI-based editor)

# GUI-Based Editors

* Trainer Editor
  * Trainer Text Editor
  * Nature & IV Calculator
  * Smogon Format Team Import/Export
  
* Pokémon Battle Sprite Editor
  * Palette Editor
  * Sprite XY-Coordinate Placement Editor
  * Sprite Shadow Placement Editor
  * Sprite Shadow Size Editor
  * Send-out Movement/Animation Editor

# Other Editors/Tools

* Narc Packer/ Unpacker

* File Compressor/ Decompressor

* Randomizer (not guaranteed to work on most recent versions, has not been tested for a while)

# Notes

* PokEditor v2 includes an auto-backup system. Upon exiting a PokEditor project, your current changes will be used to build a ROM. Up to ten backups will be stored before PokEditor asks if you would like to delete the oldest one, and you can easily rebase your project on an older version of your changes by going to "File" -> "Import ROM."
* ROMs that are exported from PokEditor v2 do not like to play nice with tools which use ndstool for ROM unpacking and repacking purposes. You can circumvent these issues by transplanting the edited NARC files from a PokEditor v2 project folder to an unpacked ROM folder from DSPRE, or in a tool such as Tinke. The tools with which errors may be encountered include but are not limited to:
  * SDSME (You shouldn't be using this regardless, use DSPRE instead)
  * [DSPRE](https://github.com/AdAstra-LD/DS-Pokemon-Rom-Editor)
  * Fly Editor
  * CrystalTile 2
  * Of course, [ndstool](https://github.com/devkitPro/ndstool) itself
* Alternatively, a PokEditor project can be opened in DSPRE by following the instructions found within the Tutorial window in PokEditor v2.
