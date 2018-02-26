# Text editor

This text editor based on rope data structures.
Provide syntax highlighting for Javascript, Haskell and Erlang.
Tested on 120Mb text.

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
* Line start: Alt + Left
* Line end: Alt + Right

**Done**:

* Chars
* Cursor
* New line
* Cursor horizontal navigation(left, right)
* Delete chars
* Current line highlighting
* Reserved js words highlighting
* Reserved erlang words highlighting
* Reserved haskell words highlighting
* Add line comments highlighting for js, hs and erlang
* Parsing file extension and set syntax automatically
* Cursor vertical navigation(up, down)
* Paired Brackets highlighting (inside visible rows)
* Selection
* Paste/Copy
* Scrolling
* Add home/end for lines
* Move cursor to file begin, move cursor to file end (like Home, End)
* Numbers, chars highlighting
* Text virtualization (big files support)
* Mouse click to set cursor
* Open files from disk
* Save/saveAs menu items
* CTRL+Z, CTRL+K - undo, redo
* CTRL+A - select all
* Add tests : rope

*Optional* (not yet implemented):

* Multi-lines comments
* Dialog box with asking if user wants to save file before exit
* More tests : UndoRedoService, TextEditorModel, Syntax highlighting
* Line numbers
* Multi-line literal string highlighting
* Parentheses highlighting
* Mouse selection

*Improvements*:

* For biggest files can read from file by parts and construct rope only for part of the file,
then using Sparks save rope into whole file