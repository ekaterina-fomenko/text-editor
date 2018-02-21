# Text editor

This text editor based on rope data structures.
Provide syntax highlighting for Javascript, Haskell and Erlang.

**Compile and run**

Requires: Maven and Java

1) Compile
mvn install

2) Run
java -cp target/text-editor-1.0-SNAPSHOT.jar com.editor.Main

**Hot Keys**:

* Open file: Ctrl + O
* Save file: Ctrl + S
* Save As file: Ctrl +Shift + S
* File Home: Alt + Up
* File End: Alt + Down
* Copy: Ctrl + C
* Paste: Ctrl + V
* Delete: Backspace
* Enter: New line
* Select: Shift + Up/Down/Right/Left
* Mouse selection
* Line start: Alt + Left
* Line end: Alt + Right

**Done**:

* Chars
* Cursor
* New line
* Cursor horizontal navigation(left, right)
* Delete chars
* Reserved js words highlighting
* Cursor vertical navigation(up, down)
* Selection
* Paste/Copy
* Reserved erlang words highlighting
* Reserved haskell words highlighting
* Scrolling
* Brackets (opened and closed)
* Move cursor to file begin, move cursor to file end (like Home, End)
* Numbers,';',',' chars highlighting
* Text virtualization (big files support)
* Mouse click to set cursor
* Mouse selection inside visible area
* Open files from disk
* Save/saveAs menu items
* Parsing file extension and set syntax automatically
* Current line highlighting
* Add line comments highlighting for js, hs and erlang
* Add home/end for lines
* Add tests

*Optional*:

* Add multi-lines comments
* Add dialog box with asking if user wants to save file before exit
* CTRL+A, Home/End for lines
* Tests
* Add line numbers
* Add multi-line leteral string highlighting (inside " ")
* Add Save\Load file option
* CTRL+Z, CTRL+K - undo, redo
* Mouse selection wider than visible area (without moving scrollbar)

*Known issues*:

* Make comments highlighting for long strings with best performance
* Copy not by CMD on Mac
* Paired brackets highlighting when one of them in literal (ex.: "adf{kj" })

*Improvements*:

* For biggest files can read from file by parts and construct rope only for part of the file,
then using Sparks save rope into whole file