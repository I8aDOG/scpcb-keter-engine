;SCP - Containment Breach

;    The game is based on the works of the SCP Foundation community (http://www.scp-wiki.net/).

;    The source code is licensed under Creative Commons Attribution-ShareAlike 3.0 License.
;    http://creativecommons.org/licenses/by-sa/3.0/

;    See Credits.txt for a list of contributors

Local InitErrorStr$ = ""
;If FileSize("bb_fmod.dll")=0 Then InitErrorStr=InitErrorStr+ "bb_fmod.dll"+Chr(13)+Chr(10)
If FileSize("fmod.dll")=0 Then InitErrorStr=InitErrorStr+ "fmod.dll"+Chr(13)+Chr(10)
If FileSize("zlibwapi.dll")=0 Then InitErrorStr=InitErrorStr+ "zlibwapi.dll"+Chr(13)+Chr(10)

If Len(InitErrorStr)>0 Then
	RuntimeError "The following DLLs were not found in the game directory:"+Chr(13)+Chr(10)+Chr(13)+Chr(10)+InitErrorStr
EndIf

Include "Src\Util\Math.bb"

Include "Src\Util\FMod.bb"

Include "Src\Util\StrictLoads.bb"
Include "Src\Util\fullscreen_window_fix.bb"
Include "Src\Util\Keys.bb"
Include "Src\Util\Input.bb"

Global OptionFile$ = "options.ini"

Include "Src\Util\Blitz_Basic_Bank.bb"
Include "Src\Util\Blitz_File_FileName.bb"
Include "Src\Util\Blitz_File_ZipApi.bb"

Include "Src\Util\DevilParticleSystem.bb"

Global ErrorFile$ = "error_log_"
Local ErrorFileInd% = 0
While FileType(ErrorFile+Str(ErrorFileInd)+".txt")<>0
	ErrorFileInd = ErrorFileInd+1
Wend
ErrorFile = ErrorFile+Str(ErrorFileInd)+".txt"

Global VersionNumber$ = "1.3.11"
Global CompatibleNumber$ = "1.3.11"

Global TotalGFXModes% = CountGfxModes3D(), GFXModes%
Dim GfxModeWidths%(TotalGFXModes), GfxModeHeights%(TotalGFXModes)

Include "Src\Util\INI.bb"
Include "Src\Util\Display.bb"
Include "Src\Util\SCPUI.bb"

Include "Src\Launcher.bb"
Launcher_Start()

SetBuffer(BackBuffer())

Const HIT_MAP% = 1, HIT_PLAYER% = 2, HIT_ITEM% = 3, HIT_APACHE% = 4, HIT_178% = 5, HIT_DEAD% = 6
SeedRnd MilliSecs()

AppTitle "SCP - Containment Breach v"+VersionNumber+" (Keter Engine)"

Launcher_Start()
Launcher_PlayStartupVideos()

