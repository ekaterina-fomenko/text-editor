# Text editor

Text editor with syntax highlighting for Javascript, Haskell and Erlang

**Hot Keys**:

* Home: Alt + Up
* End: Alt + Down
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
* Text virtualization

*Optional*:

* Comments highlighting
* CTRL+A, Home/End for lines
* Tests
* Add line numbers
* Add string highlighting (inside " ")
* Add bold font for reserved words
* Add Save\Load file option
* CTRL+Z

*Known issues*:

* Copy not by CMD on Mac
* Paired brackets highlighting when one of them in literal (ex.: "adf{kj" })
* Highlighting for numbers in names with numbers (ex.: abc2)
* Rendering 1 letter when move scroll (major)
* not rendering after CTRL+V or CTRL+DOWN, requires additional gesture to rerender (major)
* Blinking cursor on scrolling up and down (minor)
* Highlihting broken (blocker)