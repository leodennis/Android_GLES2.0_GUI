Using this program you will be able to crate a font atlas.

The font atlas can be used in an Android Project using my GUI classes.

To use a generated font class do the following:
 - Run the TextureFontAtlas tool
 - Generate a font of your choice, with a specific size (like 100)
 - Save the font
 - Copy the generated images in the drawable-nodpi folder of your android project
 - Save the generated java font class to the font package of your android project
 - Add the proper package name and fix all the imports
 - Now in the GameManager you can use the font like this: GL_Font font = new FontDroidSans100(context);
 - You defenitly want to set the defualt font like this: GL_Font.setDefaultFont(font); 
 - You might also want to set the smaller default font: GL_Font.setDefaultSmallFont(fontDroidSans64);
 - Now you the font will be used in the GL_Text, GL_Multiline_Text and GL_Buttons
