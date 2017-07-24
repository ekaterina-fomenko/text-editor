# Text editor

Text editor with syntax highlighting for Javascript, Haskell and Erlang

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

*Optional*:

* Add dialog box with asking if user wants to save file before exit
* Comments highlighting
* CTRL+A, Home/End for lines
* Tests
* Add line numbers
* Add string highlighting (inside " ")
* Add Save\Load file option
* CTRL+Z
* Mouse selection wider than visible area (without moving scrollbar)

*Known issues*:

* Copy not by CMD on Mac
* Paired brackets highlighting when one of them in literal (ex.: "adf{kj" })
* Highlighting for numbers in names with numbers (ex.: abc2)
* Blinking cursor on scrolling up and down (minor)

