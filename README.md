# Cipher
######Author: Ravish Chawla
The purpose of this app is to allow users to securely exchange files over a network.

The app uses Cipher Block Chaining(CBC) protocol to encrypt files using keys traded with the Diffie-Hellman key-exchange protocol. Files are sent through a SSL encrypted network, and stored on Facebook's Parse cloud storage.

This app is supported on all Android versions >= 4.0.3
It can be installed using the provided APK in Releases.

Usage:
  - To use, launch the app and select a user to login as.
  - Click on a listed file to download it, if it has not been downloaded before.
      - To view the file, exit the app and open a File Explorer. The file will be located in the Downloads folder.
  - Click on the message button on the bottom right to choose a file to send.
  - Select a file (in the Downloads folder).
  - Select a user to send the file to.
  - Press Encrypt to encrypt the file, then press Send to send it.

App Limitations:
  - Files sent must be located in the Downloads folder on the device. This is a limitation of Android because absolute fille paths cannot be retreived without permission of the user.
  - Files will be downloaded to the same folder, with a prefix '_'

##Project Structure
This project is divided into 5 packages:

- Activity: contains Models for Views, which control user interactivity within those panes.
- Adapters: contains Adapters for core subsystems in the app.
- LayoutAdapters: contains Models for subViews, such as dialogs and listeners.
- Returnable: contains interfaces for implementing callback functions.
- Types: contains custom data types.
