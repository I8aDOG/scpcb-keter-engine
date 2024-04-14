Include "Src\Util\AAText.bb"

InitAAFont()
;For some reason, Blitz3D doesn't load fonts that have filenames that
;don't match their "internal name" (i.e. their display name in applications
;like Word and such). As a workaround, I moved the files and renamed them so they
;can load without FastText.
Global UI_Font1%, UI_Font2%, UI_Font3%, UI_Font4%, UI_Font5%

Global UI_MenuWhite%
Global UI_MenuBlack%
Global UI_ButtonSFX%

Global UI_MenuScale#

Function UI_Init(usingAA%=True, height%=0, forceScale%=0)
	If height <= 0 Then height = GraphicsHeight
	If forceScale > 0 Then UI_MenuScale = forceScale Else UI_MenuScale = (height / 1024.0)

	UI_MenuWhite = LoadImage_Strict("GFX\menu\menuwhite.jpg")
	UI_MenuBlack = LoadImage_Strict("GFX\menu\menublack.jpg")
	UI_ButtonSFX = LoadSound_Strict("SFX\Interact\Button.ogg")

	MaskImage UI_MenuBlack, 255,255,0

	If usingAA Then
		UI_Font1% = AALoadFont("GFX\font\cour\Courier New.ttf", Int(19 * UI_MenuScale), 0,0,0)
		UI_Font2% = AALoadFont("GFX\font\courbd\Courier New.ttf", Int(58 * UI_MenuScale), 0,0,0)
		UI_Font3% = AALoadFont("GFX\font\DS-DIGI\DS-Digital.ttf", Int(22 * UI_MenuScale), 0,0,0)
		UI_Font4% = AALoadFont("GFX\font\DS-DIGI\DS-Digital.ttf", Int(60 * UI_MenuScale), 0,0,0)
		UI_Font5% = AALoadFont("GFX\font\Journal\Journal.ttf", Int(58 * UI_MenuScale), 0,0,0)
	Else
		UI_Font1% = LoadFont("GFX\font\cour\Courier New.ttf", Int(19 * UI_MenuScale), 0,0,0)
		UI_Font2% = LoadFont("GFX\font\courbd\Courier New.ttf", Int(58 * UI_MenuScale), 0,0,0)
		UI_Font3% = LoadFont("GFX\font\DS-DIGI\DS-Digital.ttf", Int(22 * UI_MenuScale), 0,0,0)
		UI_Font4% = LoadFont("GFX\font\DS-DIGI\DS-Digital.ttf", Int(60 * UI_MenuScale), 0,0,0)
		UI_Font5% = LoadFont("GFX\font\Journal\Journal.ttf", Int(58 * UI_MenuScale), 0,0,0)
	EndIf
End Function

Function DrawTiledImageRect(img%, srcX%, srcY%, srcwidth#, srcheight#, x%, y%, width%, height%)
	Local x2% = x
	While x2 < x+width
		Local y2% = y
		While y2 < y+height
			If x2 + srcwidth > x + width Then srcwidth = srcwidth - Math_Max((x2 + srcwidth) - (x + width), 1)
			If y2 + srcheight > y + height Then srcheight = srcheight - Math_Max((y2 + srcheight) - (y + height), 1)
			DrawImageRect(img, x2, y2, srcX, srcY, srcwidth, srcheight)
			y2 = y2 + srcheight
		Wend
		x2 = x2 + srcwidth
	Wend
	
End Function

Function ScaledMouseX%()
	Return Float(MouseX()-(Display_RealGraphicWidth*0.5*(1.0-Display_AspectRatioRatio)))*Float(Display_GraphicWidth)/Float(Display_RealGraphicWidth*Display_AspectRatioRatio)
End Function

Function ScaledMouseY%()
	Return Float(MouseY())*Float(Display_GraphicHeight)/Float(Display_RealGraphicHeight)
End Function

Function MouseOn%(x%, y%, width%, height%)
	If ScaledMouseX() > x And ScaledMouseX() < x + width Then
		If ScaledMouseY() > y And ScaledMouseY() < y + height Then
			Return True
		End If
	End If
	Return False
End Function

Function rInput$(aString$)
	Local value% = GetKey()
	Local length% = Len(aString$)
	
	If value = 8 Then
		value = 0
		If length > 0 Then aString$ = Left(aString, length - 1)
	EndIf
	
	If value = 13 Or value = 0 Then
		Return aString$
	ElseIf value > 0 And value < 7 Or value > 26 And value < 32 Or value = 9
		Return aString$
	Else
		aString$ = aString$ + Chr(value)
		Return aString$
	End If
End Function

Function InputBox$(x%, y%, width%, height%, Txt$, ID% = 0)
	;TextBox(x,y,width,height,Txt$)
	Color (255, 255, 255)
	DrawTiledImageRect(UI_MenuWhite, (x Mod 256), (y Mod 256), 512, 512, x, y, width, height)
	;Rect(x, y, width, height)
	Color (0, 0, 0)
	
	Local MouseOnBox% = False
	If MouseOn(x, y, width, height) Then
		Color(50, 50, 50)
		MouseOnBox = True
		If Input_WasPressedThisFrame("mouse_left") Then SelectedInputBox = ID : FlushKeys
	EndIf
	
	Rect(x + 2, y + 2, width - 4, height - 4)
	Color (255, 255, 255)	
	
	If (Not MouseOnBox) And Input_WasPressedThisFrame("mouse_left") And SelectedInputBox = ID Then SelectedInputBox = 0
	
	If SelectedInputBox = ID Then
		Txt = rInput(Txt)
		If (MilliSecs2() Mod 800) < 400 Then Rect (x + width / 2 + AAStringWidth(Txt) / 2 + 2, y + height / 2 - 5, 2, 12)
	EndIf	
	
	AAText(x + width / 2, y + height / 2, Txt, True, True)
	
	Return Txt
End Function

Function DrawFrame(x%, y%, width%, height%, xoffset%=0, yoffset%=0)
	Color 255, 255, 255
	DrawTiledImageRect(UI_MenuWhite, xoffset, (y Mod 256), 512, 512, x, y, width, height)
	DrawTiledImageRect(UI_MenuBlack, yoffset, (y Mod 256), 512, 512, x+3*UI_MenuScale, y+3*UI_MenuScale, width-6*UI_MenuScale, height-6*UI_MenuScale)	
End Function

Function DrawButton%(x%, y%, width%, height%, txt$, bigfont% = True, waitForMouseUp%=False, usingAA%=True)
	Local clicked% = False
	
	DrawFrame (x, y, width, height)
	If MouseOn(x, y, width, height) Then
		Color(30, 30, 30)
		If (Input_WasPressedThisFrame("mouse_left") And (Not waitForMouseUp)) Or (Input_WasReleasedThisFrame("mouse_left") And waitForMouseUp) Then 
			clicked = True
			PlaySound_Strict(UI_ButtonSFX)
		EndIf
		Rect(x + 4, y + 4, width - 8, height - 8)	
	Else
		Color(0, 0, 0)
	EndIf
	
	Color (255, 255, 255)
	If usingAA Then
		If bigfont Then AASetFont UI_Font2 Else AASetFont UI_Font1
		AAText(x + width / 2, y + height / 2, txt, True, True)
	Else
		If bigfont Then SetFont UI_Font2 Else SetFont UI_Font1
		Text(x + width / 2, y + height / 2, txt, True, True)
	EndIf
	
	Return clicked
End Function

Function DrawButton2%(x%, y%, width%, height%, txt$, bigfont% = True)
	Local clicked% = False
	
	DrawFrame (x, y, width, height)
	Local hit% = Input_WasPressedThisFrame("mouse_left") ; MouseHit(1)
	If MouseOn(x, y, width, height) Then
		Color(30, 30, 30)
		If hit Then clicked = True : PlaySound_Strict(UI_ButtonSFX)
		Rect(x + 4, y + 4, width - 8, height - 8)	
	Else
		Color(0, 0, 0)
	EndIf
	
	Color (255, 255, 255)
	If bigfont Then SetFont UI_Font2 Else SetFont UI_Font1
	Text(x + width / 2, y + height / 2, txt, True, True)
	
	Return clicked
End Function

Function DrawTick%(x%, y%, selected%, locked% = False)
	Local width% = 20 * UI_MenuScale, height% = 20 * UI_MenuScale
	
	Color (255, 255, 255)
	DrawTiledImageRect(UI_MenuWhite, (x Mod 256), (y Mod 256), 512, 512, x, y, width, height)
	;Rect(x, y, width, height)
	
	Local Highlight% = MouseOn(x, y, width, height) And (Not locked)
	
	If Highlight Then
		Color(50, 50, 50)
		If Input_WasPressedThisFrame("mouse_left") Then selected = (Not selected) : PlaySound_Strict (UI_ButtonSFX)
	Else
		Color(0, 0, 0)		
	End If
	
	Rect(x + 2, y + 2, width - 4, height - 4)
	
	If selected Then
		If Highlight Then
			Color 255,255,255
		Else
			Color 200,200,200
		EndIf
		DrawTiledImageRect(UI_MenuWhite, (x Mod 256), (y Mod 256), 512, 512, x + 4, y + 4, width - 8, height - 8)
		;Rect(x + 4, y + 4, width - 8, height - 8)
	EndIf
	
	Color 255, 255, 255
	
	Return selected
End Function

Function SlideBar#(x%, y%, width%, value#)
	
	If MouseDown1 And OnSliderID=0 Then
		If ScaledMouseX() >= x And ScaledMouseX() <= x + width + 14 And ScaledMouseY() >= y And ScaledMouseY() <= y + 20 Then
			value = Math_Min(Math_Max((ScaledMouseX() - x) * 100 / width, 0), 100)
		EndIf
	EndIf
	
	Color 255,255,255
	Rect(x, y, width + 14, 20,False)
	
	DrawImage(BlinkMeterIMG, x + width * value / 100.0 +3, y+3)
	
	Color 170,170,170 
	AAText (x - 50 * UI_MenuScale, y + 4*UI_MenuScale, "LOW")					
	AAText (x + width + 38 * UI_MenuScale, y+4*UI_MenuScale, "HIGH")	
	
	Return value
	
End Function

Function LimitText%(txt$, x%, y%, width%, usingAA%=True)
	Local TextLength%
	Local UnFitting%
	Local LetterWidth%
	If usingAA Then
		If txt = "" Or width = 0 Then Return 0
		TextLength = AAStringWidth(txt)
		UnFitting = TextLength - width
		If UnFitting <= 0 Then ;mahtuu
			AAText(x, y, txt)
		Else ;ei mahdu
			LetterWidth = TextLength / Len(txt)
			
			AAText(x, y, Left(txt, Math_Max(Len(txt) - UnFitting / LetterWidth - 4, 1)) + "...")
		End If
	Else
		If txt = "" Or width = 0 Then Return 0
		TextLength = StringWidth(txt)
		UnFitting = TextLength - width
		If UnFitting <= 0 Then ;mahtuu
			Text(x, y, txt)
		Else ;ei mahdu
			LetterWidth = TextLength / Len(txt)
			
			Text(x, y, Left(txt, Math_Max(Len(txt) - UnFitting / LetterWidth - 4, 1)) + "...")
		End If
	EndIf
End Function