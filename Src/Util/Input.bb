Type InputActive
    Field value$
End Type

Type Input_InputsPressed
    Field value$
End Type

Type Input_InputsActive
    Field value$
End Type

Type Input_InputsReleased
    Field value$
End Type

Function Input_Any%()
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
    If Input_GetValue(in) > 0.0 Then Return True
End Function

Function Input_WasPressedThisFrame%(in$)
    For pressed.Input_InputsPressed = Each Input_InputsPressed
        If pressed\value = in Then Return True
    Next

    Return False
End Function

Function Input_WasReleasedThisFrame%(in$)
    For released.Input_InputsReleased = Each Input_InputsReleased
        If released\value = in Then Return True
    Next

    Return False
End Function

Function Input_MouseDown%(mouse%=1)
    Return MouseDown(mouse)
End Function

Function Input_Update()
    For released.Input_InputsReleased = Each Input_InputsReleased
        Delete released
    Next

    For pressed.Input_InputsPressed = Each Input_InputsPressed
        Delete pressed
    Next

    For i = 0 To 211+5
        If Input_GetValue(KeyValue(i)) > 0.0 Then
            For active.Input_InputsActive = Each Input_InputsActive
                If active\value = KeyValue(i) Goto input_next_iteration
            Next

            Local a.Input_InputsActive = New Input_InputsActive
	        Insert a Before First Input_InputsActive
            a\value = KeyValue(i)

            Local p.Input_InputsPressed = New Input_InputsPressed
            Insert p Before First Input_InputsPressed
            p\value = KeyValue(i)
            DebugLog "Press " + p\value
        EndIf
    .input_next_iteration
    Next

    For active.Input_InputsActive = Each Input_InputsActive
        If Input_GetValue(active\value) <= 0.0 Then
            Local r.Input_InputsReleased = new Input_InputsReleased
            Insert r Before First Input_InputsReleased
            r\value = active\value

            DebugLog "Release " + r\value
            Delete active
        EndIf
    Next
End Function