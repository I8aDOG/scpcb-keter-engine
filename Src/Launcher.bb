Dim ArrowIMG(4)

Function Launcher_Start()
    Local LauncherWidth%= Math_Min(INI_GetInt(OptionFile, "launcher", "launcher width"), 1024)
    Local LauncherHeight% = Math_Min(INI_GetInt(OptionFile, "launcher", "launcher height"), 768)
    Local LauncherEnabled% = INI_GetInt(OptionFile, "launcher", "launcher enabled")

    If LauncherEnabled Then
        Display_Graphics3DExt(LauncherWidth, LauncherHeight, 0, 2)
        Display_SetAspect(LauncherWidth, LauncherHeight, LauncherWidth, LauncherHeight)

        SetBuffer BackBuffer()

        ;Font1% = LoadFont_Strict("GFX\font\cour\Courier New.ttf", 18, 0,0,0)
        ;SetFont Font1
        
        ;MenuWhite% = LoadImage_Strict("GFX\menu\menuwhite.jpg")
        ;MenuBlack% = LoadImage_Strict("GFX\menu\menublack.jpg")
        ;ButtonSFX% = LoadSound_Strict("SFX\Interact\Button.ogg")
        ;MaskImage MenuBlack, 255,255,0

        UI_Init(False, LauncherHeight, 1)

        LauncherIMG% = LoadImage_Strict("GFX\menu\launcher.jpg")
        Local i%	
        
        For i = 0 To 3
            ArrowIMG(i) = LoadImage_Strict("GFX\menu\arrow.png")
            RotateImage(ArrowIMG(i), 90 * i)
            HandleImage(ArrowIMG(i), 0, 0)
        Next
        
        For i% = 1 To TotalGFXModes
            Local samefound% = False
            For  n% = 0 To TotalGFXModes - 1
                If GfxModeWidths(n) = GfxModeWidth(i) And GfxModeHeights(n) = GfxModeHeight(i) Then samefound = True : Exit
            Next
            If samefound = False Then
                If GraphicWidth = GfxModeWidth(i) And GraphicHeight = GfxModeHeight(i) Then SelectedGFXMode = GFXModes
                GfxModeWidths(GFXModes) = GfxModeWidth(i)
                GfxModeHeights(GFXModes) = GfxModeHeight(i)
                GFXModes=GFXModes+1 
            End If
        Next
        
        BlinkMeterIMG% = LoadImage_Strict("GFX\blinkmeter.jpg")
        
        AppTitle "SCP - Containment Breach (Keter Engine) Launcher"

        Repeat
            Color 0,0,0
            Rect 0,0,LauncherWidth,LauncherHeight,True
            
            Color 255, 255, 255
            DrawImage(LauncherIMG, 0, 0)
            
            ; --- Draw Resolutions ---

            Text(20, 240 - 65, "Resolution: ")
            
            Local x% = 40
            Local y% = 270 - 65
            For i = 0 To (GFXModes - 1)
                Color 0, 0, 0
                If SelectedGFXMode = i Then Rect(x - 1, y - 1, 100, 20, False)
                
                Text(x, y, (GfxModeWidths(i) + "x" + GfxModeHeights(i)))
                If MouseOn(x - 1, y - 1, 100, 20) Then
                    Color 100, 100, 100
                    Rect(x - 1, y - 1, 100, 20, False)
                    If Input_MouseDown() Then SelectedGFXMode = i
                EndIf
                
                y=y+20
                If y >= 250 - 65 + (LauncherHeight - 80 - 260) Then y = 270 - 65 : x=x+100
            Next
            
            ;-----------------------------------------------------------------
            Color 255, 255, 255
            x = 30
            y = 369
            Rect(x - 10, y, 340, 95)
            Text(x - 10, y - 25, "Graphics:")
            
            y=y+10
            For i = 1 To CountGfxDrivers()
                Color 0, 0, 0
                If SelectedGFXDriver = i Then Rect(x - 1, y - 1, 290, 20, False)
                ;text(x, y, bbGfxDriverName(i))
                LimitText(GfxDriverName(i), x, y, 290, False)
                If MouseOn(x - 1, y - 1, 290, 20) Then
                    Color 100, 100, 100
                    Rect(x - 1, y - 1, 290, 20, False)
                    If Input_MouseDown() Then SelectedGFXDriver = i
                EndIf
                
                y=y+20
            Next
            
            Fullscreen = DrawTick(40 + 430 - 15, 260 - 55 + 5 - 8, Fullscreen, BorderlessWindowed)
            BorderlessWindowed = DrawTick(40 + 430 - 15, 260 - 55 + 35, BorderlessWindowed)
            lock% = False

            If BorderlessWindowed Or (Not Fullscreen) Then lock% = True
            Bit16Mode = DrawTick(40 + 430 - 15, 260 - 55 + 65 + 8, Bit16Mode,lock%)
            LauncherEnabled = DrawTick(40 + 430 - 15, 260 - 55 + 95 + 8, LauncherEnabled)

            If BorderlessWindowed
            Color 255, 0, 0
            Fullscreen = False
            Else
            Color 255, 255, 255
            EndIf

            Text(40 + 430 + 15, 262 - 55 + 5 - 8, "Fullscreen")
            Color 255, 255, 255
            Text(40 + 430 + 15, 262 - 55 + 35 - 8, "Borderless",False,False)
            Text(40 + 430 + 15, 262 - 55 + 35 + 12, "windowed mode",False,False)

            If BorderlessWindowed Or (Not Fullscreen)
            Color 255, 0, 0
            Bit16Mode = False
            Else
                Color 255, 255, 255
            EndIf

            Text(40 + 430 + 15, 262 - 55 + 65 + 8, "16 Bit")
            Color 255, 255, 255
            Text(40 + 430 + 15, 262 - 55 + 95 + 8, "Use launcher")
            
            If (Not BorderlessWindowed)
                If Fullscreen
                    Text(40+ 260 + 15, 262 - 55 + 140, "Current Resolution: "+(GfxModeWidths(SelectedGFXMode) + "x" + GfxModeHeights(SelectedGFXMode) + "," + (16+(16*(Not Bit16Mode)))))
                Else
                    Text(40+ 260 + 15, 262 - 55 + 140, "Current Resolution: "+(GfxModeWidths(SelectedGFXMode) + "x" + GfxModeHeights(SelectedGFXMode) + ",32"))
                EndIf
            Else
                Text(40+ 260 + 15, 262 - 55 + 140, "Current Resolution: "+GfxModeWidths(SelectedGFXMode) + "x" + GfxModeHeights(SelectedGFXMode) + ",32")
                If GfxModeWidths(SelectedGFXMode)<G_viewport_width Then
                    Text(40+ 260 + 65, 262 - 55 + 160, "(upscaled to")
                    Text(40+ 260 + 65, 262 - 55 + 180, G_viewport_width + "x" + G_viewport_height + ",32)")
                ElseIf GfxModeWidths(SelectedGFXMode)>G_viewport_width Then
                    Text(40+ 260 + 65, 262 - 55 + 160, "(downscaled to")
                    Text(40+ 260 + 65, 262 - 55 + 180, G_viewport_width + "x" + G_viewport_height + ",32)")
                EndIf
            EndIf
            
            UpdateCheckEnabled = DrawTick(LauncherWidth - 275, LauncherHeight - 50, UpdateCheckEnabled)
            Color 255,255,255
            Text LauncherWidth-250,LauncherHeight-70,"Check for"
            Text LauncherWidth-250,LauncherHeight-50,"updates on"
            Text LauncherWidth-250,LauncherHeight-30,"launch"
            
            If DrawButton(LauncherWidth - 30 - 90, LauncherHeight - 50 - 55, 100, 30, "LAUNCH", False, False, False) Then
                GraphicWidth = GfxModeWidths(SelectedGFXMode)
                GraphicHeight = GfxModeHeights(SelectedGFXMode)
                RealGraphicWidth = GraphicWidth
                RealGraphicHeight = GraphicHeight
                Exit
            EndIf
            
            If DrawButton(LauncherWidth - 30 - 90, LauncherHeight - 50, 100, 30, "EXIT", False, False, False) Then End
            Flip
        Forever
        
        INI_PutValue(OptionFile, "options", "width", GfxModeWidths(SelectedGFXMode))
        INI_PutValue(OptionFile, "options", "height", GfxModeHeights(SelectedGFXMode))
        If Fullscreen Then
            INI_PutValue(OptionFile, "options", "fullscreen", "true")
        Else
            INI_PutValue(OptionFile, "options", "fullscreen", "false")
        EndIf
        If LauncherEnabled Then
            INI_PutValue(OptionFile, "launcher", "launcher enabled", "true")
        Else
            INI_PutValue(OptionFile, "launcher", "launcher enabled", "false")
        EndIf
        If BorderlessWindowed Then
            INI_PutValue(OptionFile, "options", "borderless windowed", "true")
        Else
            INI_PutValue(OptionFile, "options", "borderless windowed", "false")
        EndIf
        If Bit16Mode Then
            INI_PutValue(OptionFile, "options", "16bit", "true")
        Else
            INI_PutValue(OptionFile, "options", "16bit", "false")
        EndIf
        INI_PutValue(OptionFile, "options", "gfx driver", SelectedGFXDriver)
        If UpdateCheckEnabled Then
            INI_PutValue(OptionFile, "options", "check for updates", "true")
        Else
            INI_PutValue(OptionFile, "options", "check for updates", "false")
        EndIf
    EndIf
End Function

Function Launcher_PlayStartupVideos()
	
	If INI_GetInt("options.ini","options","play startup video")=0 Then Return
	
	Local Cam = CreateCamera() 
	CameraClsMode Cam, 0, 1
	Local Quad = Math_CreateQuad()
	Local Texture = CreateTexture(2048, 2048, 256 Or 16 Or 32)
	EntityTexture Quad, Texture
	EntityFX Quad, 1
	CameraRange Cam, 0.01, 100
	TranslateEntity Cam, 1.0 / 2048 ,-1.0 / 2048 ,-1.0
	EntityParent Quad, Cam, 1
	
	Local ScaledGraphicHeight%
	Local Ratio# = Float(Display_RealGraphicWidth)/Float(Display_RealGraphicHeight)
	If Ratio>1.76 And Ratio<1.78
		ScaledGraphicHeight = Display_RealGraphicHeight
		DebugLog "Not Scaled"
	Else
		ScaledGraphicHeight% = Float(Display_RealGraphicWidth)/(16.0/9.0)
		DebugLog "Scaled: "+ScaledGraphicHeight
	EndIf
	
	Local moviefile$ = "GFX\menu\startup_Undertow"
	BlitzMovie_Open(moviefile$+".avi") ;Get movie size
	Local moview = BlitzMovie_GetWidth()
	Local movieh = BlitzMovie_GetHeight()
	BlitzMovie_Close()
	Local image = CreateImage(moview, movieh)
	Local SplashScreenVideo = BlitzMovie_OpenDecodeToImage(moviefile$+".avi", image, False)
	SplashScreenVideo = BlitzMovie_Play()
	Local SplashScreenAudio = StreamSound_Strict(moviefile$+".ogg",SFXVolume,0)
	Repeat
		Cls
		Math_ProjectImage(image, RealGraphicWidth, ScaledGraphicHeight, Quad, Texture)
		Flip
	Until (GetKey() Or (Not IsStreamPlaying_Strict(SplashScreenAudio)))
	StopStream_Strict(SplashScreenAudio)
	BlitzMovie_Stop()
	BlitzMovie_Close()
	FreeImage image
	
	Cls
	Flip
	
	moviefile$ = "GFX\menu\startup_TSS"
	BlitzMovie_Open(moviefile$+".avi") ;Get movie size
	moview = BlitzMovie_GetWidth()
	movieh = BlitzMovie_GetHeight()
	BlitzMovie_Close()
	image = CreateImage(moview, movieh)
	SplashScreenVideo = BlitzMovie_OpenDecodeToImage(moviefile$+".avi", image, False)
	SplashScreenVideo = BlitzMovie_Play()
	SplashScreenAudio = StreamSound_Strict(moviefile$+".ogg",SFXVolume,0)
	Repeat
		Cls
		Math_ProjectImage(image, RealGraphicWidth, ScaledGraphicHeight, Quad, Texture)
		Flip
	Until (GetKey() Or (Not IsStreamPlaying_Strict(SplashScreenAudio)))
	StopStream_Strict(SplashScreenAudio)
	BlitzMovie_Stop()
	BlitzMovie_Close()
	
	FreeTexture Texture
	FreeEntity Quad
	FreeEntity Cam
	FreeImage image
	Cls
	Flip
	
End Function