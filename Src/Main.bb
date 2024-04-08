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

Include "Util\FMod.bb"

Include "Util\StrictLoads.bb"
Include "Util\fullscreen_window_fix.bb"
Include "Util\KeyName.bb"

Global OptionFile$ = "options.ini"

Include "Util\Blitz_Basic_Bank.bb"
Include "Util\Blitz_File_FileName.bb"
Include "Util\Blitz_File_ZipApi.bb"

Include "Util\DevilParticleSystem.bb"

Global ErrorFile$ = "error_log_"
Local ErrorFileInd% = 0
While FileType(ErrorFile+Str(ErrorFileInd)+".txt")<>0
	ErrorFileInd = ErrorFileInd+1
Wend
ErrorFile = ErrorFile+Str(ErrorFileInd)+".txt"

Global VersionNumber$ = "1.3.11"
Global CompatibleNumber$ = "1.3.11"

Include "Util/SCPUI.bb"

Include "Launcher.bb"
Launcher_Start()

SetBuffer(BackBuffer())

Const HIT_MAP% = 1, HIT_PLAYER% = 2, HIT_ITEM% = 3, HIT_APACHE% = 4, HIT_178% = 5, HIT_DEAD% = 6
SeedRnd MilliSecs()

AppTitle "SCP - Containment Breach v"+VersionNumber+" (Keter Engine)"

Launcher_PlayStartupVideos()

