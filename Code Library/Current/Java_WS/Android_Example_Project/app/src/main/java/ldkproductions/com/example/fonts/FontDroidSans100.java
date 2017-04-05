package ldkproductions.com.example.fonts;

import android.content.Context;
import ldkproductions.com.example.R;
import ldkproductions.com.example.data.FontAtlas;
import ldkproductions.com.example.data.Letter;
import ldkproductions.com.example.gameelements.GL_Font;

import static ldkproductions.com.example.GameManager.context;

/**
* This class renders the font: Droid Sans with the font size 100.
* Created with the Texture Font Atlas - Tool by LDK-Productions on 04.04.2017.
*/
public class FontDroidSans100 extends GL_Font {

	public FontDroidSans100(Context context) {
		super(context);
	}

	@Override
	protected void initFont() {
		maxTextHeight = 119;
		maxDescent = 24;
		normalCapHeight = 72;
		whitespaceWidth = 26;
		spaceBetweenChars = -2;

		FontAtlas atlas1 = new FontAtlas(context,512, R.drawable.droidsans_100_1);
		Letter[] letterList1 = new Letter[62];

		letterList1[0] = new Letter(0x00c4,6,0,-1,5,63,90,-1);
		letterList1[1] = new Letter(0x00d6,68,0,6,5,63,91,5);
		letterList1[2] = new Letter(0x00dc,133,0,10,5,51,91,9);
		letterList1[3] = new Letter(0x00e4,194,-17,6,22,39,74,8);
		letterList1[4] = new Letter(0x00f6,239,-17,6,22,47,74,5);
		letterList1[5] = new Letter(0x00fc,289,-17,9,22,42,74,8);
		letterList1[6] = new Letter(0x00df,337,-12,9,17,48,79,3);
		letterList1[7] = new Letter(0x00c0,401,5,-1,0,63,95,-1);
		letterList1[8] = new Letter(0x00c2,201,85,-1,0,63,95,-1);
		letterList1[9] = new Letter(0x00c6,270,67,-1,23,81,72,5);
		letterList1[10] = new Letter(0x00c7,-1,79,6,22,51,97,3);
		letterList1[11] = new Letter(0x00c8,346,90,10,0,38,95,5);
		letterList1[12] = new Letter(0x00c9,52,102,10,0,38,95,5);
		letterList1[13] = new Letter(0x00ca,96,102,10,0,38,95,5);
		letterList1[14] = new Letter(0x00cb,140,97,10,5,38,90,5);
		letterList1[15] = new Letter(0x00ce,468,5,1,0,33,95,0);
		letterList1[16] = new Letter(0x00cf,397,101,3,5,28,90,3);
		letterList1[17] = new Letter(0x00d4,428,106,6,0,63,96,5);
		letterList1[18] = new Letter(0x0152,263,146,6,22,78,74,5);
		letterList1[19] = new Letter(0x00d9,184,186,10,0,51,96,9);
		letterList1[20] = new Letter(0x00db,343,202,10,0,51,96,9);
		letterList1[21] = new Letter(0x0178,63,198,-1,5,55,90,-1);
		letterList1[22] = new Letter(0x00e0,117,186,6,17,39,79,8);
		letterList1[23] = new Letter(0x00e2,-1,187,6,17,39,79,8);
		letterList1[24] = new Letter(0x00e6,404,169,6,39,72,57,5);
		letterList1[25] = new Letter(0x00e7,245,209,6,39,37,80,3);
		letterList1[26] = new Letter(0x00e8,288,231,6,17,43,79,5);
		letterList1[27] = new Letter(0x00e9,404,254,6,17,43,79,5);
		letterList1[28] = new Letter(0x00ea,453,254,6,17,43,79,5);
		letterList1[29] = new Letter(0x00eb,117,266,6,22,43,74,5);
		letterList1[30] = new Letter(0x00ee,174,271,-2,17,33,78,-5);
		letterList1[31] = new Letter(0x00ef,211,266,0,22,28,73,-2);
		letterList1[32] = new Letter(0x00f4,-1,272,6,17,47,79,5);
		letterList1[33] = new Letter(0x0153,288,294,6,39,80,57,5);
		letterList1[34] = new Letter(0x00f9,49,282,9,17,42,79,8);
		letterList1[35] = new Letter(0x00fb,236,317,9,17,42,79,8);
		letterList1[36] = new Letter(0x00ff,381,334,-1,22,51,97,-1);
		letterList1[37] = new Letter(0x0026,432,335,5,21,63,75,2);
		letterList1[38] = new Letter(0x0024,100,350,6,18,43,83,6);
		letterList1[39] = new Letter(0x20ac,153,350,2,22,52,74,1);
		letterList1[40] = new Letter(0x0040,0,361,5,23,78,81,4);
		letterList1[41] = new Letter(0x0023,291,373,2,23,62,72,1);
		letterList1[42] = new Letter(0x00b0,207,397,6,22,32,32,5);
		letterList1[43] = new Letter(0x00bb,433,389,4,48,42,41,3);
		letterList1[44] = new Letter(0x00ab,152,404,3,48,42,41,4);
		letterList1[45] = new Letter(0x2013,339,241,4,63,43,10,3);
		letterList1[46] = new Letter(0x2014,1,408,4,63,93,10,3);
		letterList1[47] = new Letter(0x00b7,244,134,7,52,12,14,8);
		letterList1[48] = new Letter(0x002b,198,420,5,37,46,46,4);
		letterList1[49] = new Letter(0x003d,356,414,5,45,46,29,4);
		letterList1[50] = new Letter(0x2026,248,392,7,82,67,14,6);
		letterList1[51] = new Letter(0x00b4,149,181,19,17,20,17,19);
		letterList1[52] = new Letter(0x002e,481,126,7,82,13,14,7);
		letterList1[53] = new Letter(0x002c,165,139,3,82,15,26,7);
		letterList1[54] = new Letter(0x0022,244,396,7,23,27,27,6);
		letterList1[55] = new Letter(0x201e,101,375,3,82,34,26,7);
		letterList1[56] = new Letter(0x003b,482,397,3,40,16,68,8);
		letterList1[57] = new Letter(0x003a,354,356,7,40,13,56,7);
		letterList1[58] = new Letter(0x002d,209,304,4,63,25,10,3);
		letterList1[59] = new Letter(0x005f,413,381,0,103,42,8,-1);
		letterList1[60] = new Letter(0x0027,244,183,7,23,10,27,6);
		letterList1[61] = new Letter(0x006c,80,366,9,18,9,77,8);

		atlas1.setLetterList(letterList1);


		fontAtlasList.add(atlas1);

		FontAtlas atlas2 = new FontAtlas(context,512,R.drawable.droidsans_100_2);
		Letter[] letterList2 = new Letter[59];

		letterList2[0] = new Letter(0x002f,5,-18,0,23,37,72,0);
		letterList2[1] = new Letter(0x005c,48,-18,0,23,37,72,0);
		letterList2[2] = new Letter(0x005b,83,-18,8,23,21,88,1);
		letterList2[3] = new Letter(0x005d,116,-18,2,23,21,88,7);
		letterList2[4] = new Letter(0x003c,140,-29,5,34,46,49,4);
		letterList2[5] = new Letter(0x003e,192,-29,5,34,46,49,4);
		letterList2[6] = new Letter(0x007b,247,-18,2,23,31,88,2);
		letterList2[7] = new Letter(0x007d,283,-18,3,23,31,88,1);
		letterList2[8] = new Letter(0x007c,299,-13,24,18,8,101,23);
		letterList2[9] = new Letter(0x201c,336,-18,1,23,34,26,1);
		letterList2[10] = new Letter(0x0021,370,-18,7,23,13,73,7);
		letterList2[11] = new Letter(0x003f,394,-17,2,22,39,74,2);
		letterList2[12] = new Letter(0x0028,437,-18,4,23,23,88,3);
		letterList2[13] = new Letter(0x0029,467,-18,3,23,24,88,3);
		letterList2[14] = new Letter(0x002a,142,42,3,18,48,46,4);
		letterList2[15] = new Letter(0x0025,0,61,5,22,74,74,4);
		letterList2[16] = new Letter(0x2260,332,52,5,32,46,55,4);
		letterList2[17] = new Letter(0x005e,83,76,2,23,49,46,2);
		letterList2[18] = new Letter(0x00b1,384,48,5,37,46,58,4);
		letterList2[19] = new Letter(0x2265,194,65,5,34,46,61,4);
		letterList2[20] = new Letter(0x2264,246,65,5,34,46,61,4);
		letterList2[21] = new Letter(0x03c0,440,59,1,40,60,56,2);
		letterList2[22] = new Letter(0x221e,297,102,6,43,60,33,5);
		letterList2[23] = new Letter(0x00f7,135,76,5,36,46,47,4);
		letterList2[24] = new Letter(0x0030,364,127,5,22,46,74,4);
		letterList2[25] = new Letter(0x0031,76,128,9,23,26,72,20);
		letterList2[26] = new Letter(0x0032,416,139,5,22,44,73,6);
		letterList2[27] = new Letter(0x0033,1,141,4,22,46,74,5);
		letterList2[28] = new Letter(0x0034,116,142,1,23,53,72,1);
		letterList2[29] = new Letter(0x0035,170,143,6,23,44,73,5);
		letterList2[30] = new Letter(0x0036,220,144,6,22,45,74,4);
		letterList2[31] = new Letter(0x0037,273,161,4,23,47,72,4);
		letterList2[32] = new Letter(0x0038,52,207,5,22,46,74,4);
		letterList2[33] = new Letter(0x0039,325,207,5,22,46,73,4);
		letterList2[34] = new Letter(0x0041,383,217,-1,23,63,72,-1);
		letterList2[35] = new Letter(0x0042,441,217,10,23,47,72,5);
		letterList2[36] = new Letter(0x0043,103,221,6,22,51,74,3);
		letterList2[37] = new Letter(0x0044,156,222,10,23,53,72,5);
		letterList2[38] = new Letter(0x0045,-5,220,10,23,38,72,5);
		letterList2[39] = new Letter(0x0046,215,223,10,23,38,72,1);
		letterList2[40] = new Letter(0x0047,263,286,6,22,57,74,6);
		letterList2[41] = new Letter(0x0048,39,286,10,23,51,72,9);
		letterList2[42] = new Letter(0x0049,468,138,3,23,27,72,4);
		letterList2[43] = new Letter(0x004a,341,285,-9,23,28,91,8);
		letterList2[44] = new Letter(0x004b,356,295,10,23,49,72,-1);
		letterList2[45] = new Letter(0x004c,411,295,10,23,37,72,2);
		letterList2[46] = new Letter(0x004d,96,300,10,23,68,72,9);
		letterList2[47] = new Letter(0x004e,170,301,10,23,54,72,9);
		letterList2[48] = new Letter(0x004f,-1,365,6,22,63,74,5);
		letterList2[49] = new Letter(0x0050,230,365,10,23,43,72,5);
		letterList2[50] = new Letter(0x0051,360,374,6,22,63,90,5);
		letterList2[51] = new Letter(0x0052,425,373,10,23,49,72,0);
		letterList2[52] = new Letter(0x0053,69,379,5,22,43,74,4);
		letterList2[53] = new Letter(0x0054,122,378,1,23,50,72,1);
		letterList2[54] = new Letter(0x0055,169,379,10,23,51,73,9);
		letterList2[55] = new Letter(0x0056,290,382,-1,23,59,72,-1);
		letterList2[56] = new Letter(0x0061,458,279,6,39,39,57,8);
		letterList2[57] = new Letter(0x0063,-1,282,6,39,37,57,3);
		letterList2[58] = new Letter(0x0069,482,361,8,20,11,75,7);

		atlas2.setLetterList(letterList2);


		fontAtlasList.add(atlas2);

		FontAtlas atlas3 = new FontAtlas(context,512,R.drawable.droidsans_100_3);
		Letter[] letterList3 = new Letter[26];

		letterList3[0] = new Letter(0x0057,5,-18,0,23,88,72,0);
		letterList3[1] = new Letter(0x0058,100,-18,-1,23,57,72,-1);
		letterList3[2] = new Letter(0x0059,163,-18,-1,23,55,72,-1);
		letterList3[3] = new Letter(0x005a,219,-18,4,23,47,72,3);
		letterList3[4] = new Letter(0x0062,267,-13,9,18,45,78,5);
		letterList3[5] = new Letter(0x0064,321,-13,6,18,45,78,8);
		letterList3[6] = new Letter(0x0065,372,-34,6,39,43,57,5);
		letterList3[7] = new Letter(0x0066,425,-12,2,17,35,78,-4);
		letterList3[8] = new Letter(0x0067,3,44,2,39,48,80,2);
		letterList3[9] = new Letter(0x0068,369,50,9,18,42,77,8);
		letterList3[10] = new Letter(0x006a,471,-15,-3,20,22,99,7);
		letterList3[11] = new Letter(0x006b,50,65,9,18,42,77,-1);
		letterList3[12] = new Letter(0x006d,98,44,9,39,73,56,8);
		letterList3[13] = new Letter(0x006e,177,44,9,39,42,56,8);
		letterList3[14] = new Letter(0x006f,228,50,6,39,47,57,5);
		letterList3[15] = new Letter(0x0070,278,50,9,39,45,80,5);
		letterList3[16] = new Letter(0x0071,420,71,6,39,45,80,8);
		letterList3[17] = new Letter(0x0072,329,50,9,39,30,56,1);
		letterList3[18] = new Letter(0x0073,102,106,5,39,35,57,5);
		letterList3[19] = new Letter(0x0074,147,117,1,28,32,68,1);
		letterList3[20] = new Letter(0x0075,177,105,9,40,42,56,8);
		letterList3[21] = new Letter(0x0076,339,111,-1,40,50,55,-1);
		letterList3[22] = new Letter(0x0077,5,129,0,40,75,55,0);
		letterList3[23] = new Letter(0x0078,232,112,2,40,46,55,2);
		letterList3[24] = new Letter(0x0079,395,156,-1,40,51,79,-1);
		letterList3[25] = new Letter(0x007a,282,135,4,40,36,55,4);

		atlas3.setLetterList(letterList3);


		fontAtlasList.add(atlas3);

	}
}