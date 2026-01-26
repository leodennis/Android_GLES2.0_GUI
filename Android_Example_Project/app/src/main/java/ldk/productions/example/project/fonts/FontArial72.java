package ldk.productions.example.project.fonts;

import android.content.Context;

import ldk.productions.example.project.R;
import ldk.productions.example.project.data.FontAtlas;
import ldk.productions.example.project.data.Letter;
import ldk.productions.example.project.gameelements.GL_Font;

/**
* This class renders the font: Arial with the font size 72.
* Created with the Texture Font Atlas - Tool by LDK-Productions on 25.08.2021.
*/
public class FontArial72 extends GL_Font {

	public FontArial72(Context context) {
		super(context);
	}

	@Override
	protected void initFont() {
		maxTextHeight = 81;
		maxDescent = 15;
		normalCapHeight = 53;
		whitespaceWidth = 20;
		spaceBetweenChars = -1;

		FontAtlas atlas1 = new FontAtlas(context,512, R.drawable.arial_72_1);
		Letter[] letterList1 = new Letter[100];

		letterList1[0] = new Letter(0x0026,2,-7,3,12,43,55,2);
		letterList1[1] = new Letter(0x0024,51,-5,3,10,35,63,2);
		letterList1[2] = new Letter(0x20ac,96,-7,-1,12,40,55,1);
		letterList1[3] = new Letter(0x0040,137,-7,4,12,66,69,3);
		letterList1[4] = new Letter(0x0023,213,-8,0,13,40,53,0);
		letterList1[5] = new Letter(0x00b0,255,-7,4,12,21,21,4);
		letterList1[6] = new Letter(0x2013,286,-38,0,43,41,7,-1);
		letterList1[7] = new Letter(0x2014,333,-38,0,43,73,7,-1);
		letterList1[8] = new Letter(0x00b7,404,-32,8,37,7,7,9);
		letterList1[9] = new Letter(0x002f,425,-8,0,13,21,53,-1);
		letterList1[10] = new Letter(0x005c,452,-8,0,13,21,53,-1);
		letterList1[11] = new Letter(0x002b,282,-5,4,23,35,35,3);
		letterList1[12] = new Letter(0x003c,323,-5,4,23,35,35,3);
		letterList1[13] = new Letter(0x003e,364,-5,4,23,35,35,3);
		letterList1[14] = new Letter(0x002e,473,-54,6,59,7,7,7);
		letterList1[15] = new Letter(0x002c,486,-54,6,59,7,18,7);
		letterList1[16] = new Letter(0x0022,476,16,3,13,20,19,3);
		letterList1[17] = new Letter(0x201e,257,-27,2,59,18,18,4);
		letterList1[18] = new Letter(0x201c,476,41,3,13,17,17,4);
		letterList1[19] = new Letter(0x0021,401,5,8,13,7,53,7);
		letterList1[20] = new Letter(0x003f,256,47,3,12,35,54,2);
		letterList1[21] = new Letter(0x003b,294,32,6,27,7,50,7);
		letterList1[22] = new Letter(0x003a,307,32,6,27,7,39,7);
		letterList1[23] = new Letter(0x002d,324,15,2,44,21,7,1);
		letterList1[24] = new Letter(0x005f,354,-14,-1,73,43,7,-2);
		letterList1[25] = new Letter(0x0027,210,51,3,13,7,19,4);
		letterList1[26] = new Letter(0x0028,222,51,4,13,17,67,3);
		letterList1[27] = new Letter(0x0029,419,51,3,13,18,67,3);
		letterList1[28] = new Letter(0x002a,444,51,2,13,23,23,3);
		letterList1[29] = new Letter(0x0025,322,60,4,12,57,55,3);
		letterList1[30] = new Letter(0x005e,3,53,2,13,29,29,2);
		letterList1[31] = new Letter(0x003d,91,38,4,28,35,23,3);
		letterList1[32] = new Letter(0x0030,37,61,3,13,35,54,2);
		letterList1[33] = new Letter(0x0031,381,64,8,13,20,53,12);
		letterList1[34] = new Letter(0x0032,134,67,2,13,35,53,3);
		letterList1[35] = new Letter(0x0033,174,76,3,13,35,54,2);
		letterList1[36] = new Letter(0x0034,445,79,1,14,37,52,2);
		letterList1[37] = new Letter(0x0035,78,81,3,14,35,53,2);
		letterList1[38] = new Letter(0x0036,247,106,2,13,36,54,2);
		letterList1[39] = new Letter(0x0037,288,119,3,14,34,52,3);
		letterList1[40] = new Letter(0x0038,328,120,3,13,35,54,2);
		letterList1[41] = new Letter(0x0039,2,121,3,13,35,54,2);
		letterList1[42] = new Letter(0x00c0,373,137,-1,0,49,66,0);
		letterList1[43] = new Letter(0x00c2,123,139,-1,0,49,66,0);
		letterList1[44] = new Letter(0x00c6,428,138,-1,13,70,53,3);
		letterList1[45] = new Letter(0x00c7,173,137,4,12,45,68,3);
		letterList1[46] = new Letter(0x00c8,40,154,6,0,39,66,3);
		letterList1[47] = new Letter(0x00c9,222,179,6,0,39,66,3);
		letterList1[48] = new Letter(0x00ca,267,191,6,0,39,66,3);
		letterList1[49] = new Letter(0x00cb,312,189,6,4,39,62,3);
		letterList1[50] = new Letter(0x00ce,92,154,-1,0,20,66,1);
		letterList1[51] = new Letter(0x00cf,4,190,1,4,19,62,0);
		letterList1[52] = new Letter(0x00d4,359,209,4,0,49,67,3);
		letterList1[53] = new Letter(0x0152,414,198,4,12,67,55,1);
		letterList1[54] = new Letter(0x00d9,111,211,6,0,41,67,5);
		letterList1[55] = new Letter(0x00db,158,223,6,0,41,67,5);
		letterList1[56] = new Letter(0x00dc,24,222,6,4,41,63,5);
		letterList1[57] = new Letter(0x0178,211,247,0,4,47,62,0);
		letterList1[58] = new Letter(0x00e0,74,213,3,13,34,54,3);
		letterList1[59] = new Letter(0x00e2,315,248,3,13,34,54,3);
		letterList1[60] = new Letter(0x00e6,416,245,2,26,60,41,2);
		letterList1[61] = new Letter(0x00e7,261,237,3,26,33,54,0);
		letterList1[62] = new Letter(0x00e8,356,269,2,13,36,54,2);
		letterList1[63] = new Letter(0x00e9,115,271,2,13,36,54,2);
		letterList1[64] = new Letter(0x00ea,3,282,2,13,36,54,2);
		letterList1[65] = new Letter(0x00eb,45,278,2,17,36,50,2);
		letterList1[66] = new Letter(0x00ee,484,258,0,13,20,53,0);
		letterList1[67] = new Letter(0x00ef,88,269,1,17,19,49,0);
		letterList1[68] = new Letter(0x00f4,156,283,3,13,35,54,2);
		letterList1[69] = new Letter(0x0153,398,292,2,26,63,41,3);
		letterList1[70] = new Letter(0x00f9,195,306,5,13,31,54,4);
		letterList1[71] = new Letter(0x00fb,298,308,5,13,31,54,4);
		letterList1[72] = new Letter(0x00fc,232,306,5,17,31,50,4);
		letterList1[73] = new Letter(0x00ff,468,313,1,17,34,64,0);
		letterList1[74] = new Letter(0x00c4,341,338,-1,4,49,62,0);
		letterList1[75] = new Letter(0x00d6,85,340,4,4,49,63,3);
		letterList1[76] = new Letter(0x00e4,44,334,3,17,34,50,3);
		letterList1[77] = new Letter(0x00f6,2,338,3,17,35,50,2);
		letterList1[78] = new Letter(0x00df,139,344,5,12,37,55,2);
		letterList1[79] = new Letter(0x0041,396,352,-1,13,49,53,0);
		letterList1[80] = new Letter(0x0042,181,366,6,13,39,53,3);
		letterList1[81] = new Letter(0x0043,228,367,4,12,45,55,3);
		letterList1[82] = new Letter(0x0044,277,368,6,13,43,53,3);
		letterList1[83] = new Letter(0x0045,444,387,6,13,39,53,3);
		letterList1[84] = new Letter(0x0046,40,394,6,13,35,53,3);
		letterList1[85] = new Letter(0x0047,328,398,4,12,48,55,4);
		letterList1[86] = new Letter(0x0048,81,400,6,13,41,53,5);
		letterList1[87] = new Letter(0x0049,483,64,6,13,8,53,6);
		letterList1[88] = new Letter(0x004a,3,398,2,13,29,54,5);
		letterList1[89] = new Letter(0x004b,128,404,6,13,43,53,-1);
		letterList1[90] = new Letter(0x004c,380,411,6,13,33,53,1);
		letterList1[91] = new Letter(0x004d,178,427,5,13,50,53,4);
		letterList1[92] = new Letter(0x004e,233,427,6,13,41,53,5);
		letterList1[93] = new Letter(0x0050,280,427,6,13,39,53,3);
		letterList1[94] = new Letter(0x0061,422,433,3,26,34,41,3);
		letterList1[95] = new Letter(0x0063,462,433,3,26,33,41,0);
		letterList1[96] = new Letter(0x0069,486,197,5,13,7,53,4);
		letterList1[97] = new Letter(0x006c,490,387,5,13,7,53,4);
		letterList1[98] = new Letter(0x006e,35,440,5,26,31,40,4);
		letterList1[99] = new Letter(0x0072,269,296,5,27,20,39,-1);

		atlas1.setLetterList(letterList1);


		fontAtlasList.add(atlas1);

		FontAtlas atlas2 = new FontAtlas(context,256,R.drawable.arial_72_2);
		Letter[] letterList2 = new Letter[21];

		letterList2[0] = new Letter(0x004f,1,-7,4,12,49,55,3);
		letterList2[1] = new Letter(0x0051,56,-7,4,12,48,59,4);
		letterList2[2] = new Letter(0x0052,108,-8,6,13,44,53,2);
		letterList2[3] = new Letter(0x0053,160,-7,4,12,40,55,4);
		letterList2[4] = new Letter(0x0054,112,51,2,13,42,53,1);
		letterList2[5] = new Letter(0x0055,-1,53,6,13,41,54,5);
		letterList2[6] = new Letter(0x0056,162,53,0,13,49,53,-1);
		letterList2[7] = new Letter(0x0057,52,110,0,13,71,53,1);
		letterList2[8] = new Letter(0x0058,130,112,-1,13,47,53,1);
		letterList2[9] = new Letter(0x0059,182,112,0,13,47,53,0);
		letterList2[10] = new Letter(0x005a,4,169,1,13,42,53,1);
		letterList2[11] = new Letter(0x0062,206,-8,4,13,34,54,2);
		letterList2[12] = new Letter(0x0064,51,169,2,13,34,54,4);
		letterList2[13] = new Letter(0x0065,50,44,2,26,36,41,2);
		letterList2[14] = new Letter(0x0066,215,53,2,12,21,54,-2);
		letterList2[15] = new Letter(0x0067,91,158,2,26,34,55,4);
		letterList2[16] = new Letter(0x0068,128,171,5,13,31,53,4);
		letterList2[17] = new Letter(0x006a,239,112,-4,13,15,68,4);
		letterList2[18] = new Letter(0x006b,166,171,4,13,31,53,1);
		letterList2[19] = new Letter(0x006f,2,100,3,26,35,41,2);
		letterList2[20] = new Letter(0x0073,205,173,2,26,32,41,2);

		atlas2.setLetterList(letterList2);


		fontAtlasList.add(atlas2);

		FontAtlas atlas3 = new FontAtlas(context,256,R.drawable.arial_72_3);
		Letter[] letterList3 = new Letter[10];

		letterList3[0] = new Letter(0x006d,1,-21,4,26,51,40,4);
		letterList3[1] = new Letter(0x0070,58,-21,4,26,34,54,2);
		letterList3[2] = new Letter(0x0071,100,-21,2,26,34,54,4);
		letterList3[3] = new Letter(0x0074,141,-10,1,15,19,52,0);
		letterList3[4] = new Letter(0x0075,162,-22,5,27,31,40,4);
		letterList3[5] = new Letter(0x0076,203,-22,1,27,35,39,1);
		letterList3[6] = new Letter(0x0077,168,24,-1,27,53,39,-1);
		letterList3[7] = new Letter(0x0078,5,24,0,27,36,39,-1);
		letterList3[8] = new Letter(0x0079,46,38,1,27,34,54,0);
		letterList3[9] = new Letter(0x007a,86,38,1,27,34,39,1);

		atlas3.setLetterList(letterList3);


		fontAtlasList.add(atlas3);

	}
}