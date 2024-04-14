Include "Src\Util\fullscreen_window_fix.bb"

Global Display_GraphicWidth%
Global Display_GraphicHeight%
Global Display_Depth% = 0, Display_Fullscreen% = INI_GetInt(OptionFile, "options", "fullscreen")

Global Display_BorderlessWindowed% = INI_GetInt(OptionFile, "options", "borderless windowed")
Global Display_RealGraphicWidth%,Display_RealGraphicHeight%
Global Display_AspectRatioRatio#

Function Display_SetAspect(width%, height%, graphicw%=0, graphich%=0)
	Display_RealGraphicWidth = width
	Display_RealGraphicHeight = height
    If graphicw <= 0 Then Display_GraphicWidth = INI_GetInt(OptionFile, "options", "width") Else Display_GraphicWidth = graphicw
    If graphich <= 0 Then Display_GraphicHeight = INI_GetInt(OptionFile, "options", "height") Else Display_GraphicHeight = graphich
		
	Display_AspectRatioRatio = (Float(Display_GraphicWidth)/Float(Display_GraphicHeight))/(Float(Display_RealGraphicWidth)/Float(Display_RealGraphicHeight))
End Function

Function Display_Graphics3DExt%(width%,height%,depth%=32,mode%=2)
	;If FE_InitExtFlag = 1 Then DeInitExt() ;prevent FastExt from breaking itself
	Graphics3D width,height,depth,mode
	InitFastResize()
	;InitExt()
	AntiAlias INI_GetInt(OptionFile,"options","antialias")
	;TextureAnisotropy% (GetINIInt(OptionFile,"options","anisotropy"),-1)
End Function

Function Display_Init()
	If BorderlessWindowed
		DebugLog "Using Borderless Windowed Mode"
		Display_Graphics3DExt G_viewport_width, G_viewport_height, 0, 2
		
		; -- Change the window style to 'WS_POPUP' and then set the window position to force the style to update.
		api_SetWindowLong( G_app_handle, C_GWL_STYLE, C_WS_POPUP )
		api_SetWindowPos( G_app_handle, C_HWND_TOP, G_viewport_x, G_viewport_y, G_viewport_width, G_viewport_height, C_SWP_SHOWWINDOW )
		
		Display_SetAspect(G_viewport_width, G_viewport_height)
		; RealGraphicWidth = G_viewport_width
		; RealGraphicHeight = G_viewport_height
		
		; AspectRatioRatio = (Float(GraphicWidth)/Float(GraphicHeight))/(Float(RealGraphicWidth)/Float(RealGraphicHeight))
		
		Display_Fullscreen = False
	Else
		Display_SetAspect(INI_GetInt(OptionFile, "options", "width"), INI_GetInt(OptionFile, "options", "height"))
		If Display_Fullscreen Then
			Display_Graphics3DExt(Display_GraphicWidth, Display_GraphicHeight, (16*Bit16Mode), 1)
		Else
			Display_Graphics3DExt(Display_GraphicWidth, Display_GraphicHeight, 0, 2)
		End If
	EndIf
End Function