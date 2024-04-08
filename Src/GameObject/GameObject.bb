Type GameObject
    Field name$
    Field x#, y#, z#
    Field id%
End Type

Include "Test.bb"

Function GameObject_Create(objectName$)
    obj.GameObject = new GameObject
    obj\name = objectName

    Select objectName
        Case "test"
            Test_Create(obj)
            Return
    End Select

    ; Failed to find GameObject create function
    Delete obj
End Function

Function GameObject_Update(this.GameObject)
    Select this\name
        Case "test"
            Test_Update(this)
    End Select
End Function