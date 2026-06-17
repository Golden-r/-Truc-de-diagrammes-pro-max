@echo off
rem Vérifier que src existe
if not exist src (
	echo Le dossier src n'existe pas.
	exit /b 1
)

rem Générer compile.list avec PowerShell (tri inversé)
powershell -Command "Get-ChildItem -Path src -Recurse -Filter *.java | ForEach-Object { './' + $_.FullName.Replace((Get-Location).Path + '\', '').Replace('\', '/') } | Sort-Object -Descending | Out-File -Encoding ASCII compile.list"