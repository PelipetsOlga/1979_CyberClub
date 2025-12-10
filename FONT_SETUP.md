# Manrope Font Setup Guide

## Steps to Complete Font Setup

### 1. Download Manrope Font Files
Download the Manrope font family from [Google Fonts](https://fonts.google.com/specimen/Manrope) or the official repository.

You'll need these font files:
- `Manrope-Light.ttf` (FontWeight.Light - 300)
- `Manrope-Regular.ttf` (FontWeight.Normal - 400)
- `Manrope-Medium.ttf` (FontWeight.Medium - 500)
- `Manrope-SemiBold.ttf` (FontWeight.SemiBold - 600)
- `Manrope-Bold.ttf` (FontWeight.Bold - 700)
- `Manrope-ExtraBold.ttf` (FontWeight.ExtraBold - 800)

### 2. Create Font Directory
Create the font directory if it doesn't exist:
```
app/src/main/res/font/
```

### 3. Add Font Files
Place the downloaded font files in `app/src/main/res/font/` with these exact names:
- `manrope_light.ttf`
- `manrope_regular.ttf`
- `manrope_medium.ttf`
- `manrope_semibold.ttf`
- `manrope_bold.ttf`
- `manrope_extrabold.ttf`

**Important:** Use lowercase with underscores (snake_case) for the file names, as shown above.

### 4. Sync Project
After adding the font files:
1. In Android Studio, right-click on the `res` folder
2. Select "Synchronize 'res' with File System" or
3. Use "File" → "Sync Project with Gradle Files"

### 5. Verify Setup
The font will automatically be used throughout the app via `MaterialTheme.typography` since all typography styles in `Theme.kt` are configured to use Manrope.

## How It Works

- All text using `MaterialTheme.typography.*` will automatically use Manrope
- The `AppTypography` in `Theme.kt` defines all text styles with Manrope font family
- No need to manually specify the font in individual composables

## Testing

After adding the fonts, rebuild the project and check that all text displays with Manrope font.
