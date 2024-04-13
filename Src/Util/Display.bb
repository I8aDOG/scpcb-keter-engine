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