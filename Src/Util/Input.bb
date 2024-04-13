Type InputActive
    Field value$
End Type

Function Input_Any()
    Return GetKey<>0 Or Input_MouseDown()
End Function

Function Input_GetValue#(in$)
    If in = "mouse_left" And Input_MouseDown() Then Return 1.0
    If in = "mouse_right" And Input_MouseDown(2) Then Return 1.0

    Local kb$ = Key_GetValue(in)
    If in >= 0 And KeyDown(kb) Then Return 1.0

    Return 0.0
End Function

Function Input_IsPressed%(in$)
    If Input_GetValue(in) > 0 Then Return True
End Function

Function Input_MouseDown(mouse%=1)
    Return MouseHit(mouse)
End Function

Function Input_Update()

End Function