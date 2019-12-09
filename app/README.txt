Our app requires a log in through google.
Google authentication requires the latest version of the Google Play Store on the emulator to work.
You can do this through the SDK or manually through the gradle.
If your Google Play Store isn't fully up to date, you will not be able to log in with google's services.
If so, please test it with a non-emulated android phone. The lowest APK for our app is 26.

Because the app deals with Firebase over the internet, and with streaming videos, upload/download loading times may be slow.
Please wait for a toast message to appear after you submit a video, which will confirm your upload, before venturing to other pages.
Since we didn't implement quality compressing and video cropping, if you upload a high quality video from an HDR camera or such,
    it will load slower than the other default videos.
We actually had to sign up for the free trial for google cloud,
    since we passed our quota when testing video uploading on our app,
    which caused all of storage to be completely inaccessible (yikes!) until we got the trial.
    So don't worry about testing the upload feature for yourself as many times as you like.

The Home video feed currently displays the usernames of the user that uploaded the video instead of the display name
    so that you can test the functionality of our app preventing a user from taking an existing username.

Known problems/incomplete features (not necessarily bugs) are:
-- Likes on the Home fragment videos can be infinitely added.
-- Videos on the Home fragment, depending on the video quality & current workload,
    may buffer the video's audio before buffering the video itself,
    causing the full video audio to play, sometimes with or without the initial video thumbnail loaded,
    before the actual video plays.
-- The camera cannot be switched on the Record fragment.
-- Venturing to other fragments after sending in a video before the upload is confirmed may crash the app
-- Videos in the User fragment cannot be clicked on.

If you want to have access our Firebase consoles, email Paige, or any of the other teammates.