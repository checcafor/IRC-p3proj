# IRC-p3proj
<a name="readme-top"></a>
<!-- PROJECT SHIELDS -->
[![Licence](https://img.shields.io/github/license/Ileriayo/markdown-badges?style=for-the-badge)](./LICENSE)
![GitHub][GitHub.com]

<!-- PROJECT LOGO -->
<br />
<div align="center">
  <a href="https://github.com/checcafor/IRC-p3proj">
    <img src="static/img/readme/logo.png" alt="Logo" width="200" height="80">
  </a>

  <!-- <h3 align="center">Ko-pilot</h3> -->
  <br>

  <p align="center">
    An awesome way to move green!
    <br />
    <a href="#explore"><strong>Explore the docs »</strong></a>
    <br>

  </p>
</div>

<!-- ABOUT THE PROJECT -->
## About The Project

<a name="explore"></a>
<div align="center" style="margin-top: 30px">
    <img src="static/img/readme/preview.png" alt="Logo">
</div>

"Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- BUILD WITH -->
## Built With

"Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."

### Back and Front:

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)

### DB Support:

![MySQL](https://img.shields.io/badge/MySQL-00000F?style=for-the-badge&logo=mysql&logoColor=white)

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- PROJECT STRUCTURE -->
## Project Structure

"Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."

```bash
   📦 IRC chat
    ├─ src
    │  ├─ 🔌 dbconnector
    │  │  ├─ DatabaseConnector.java
    │  │  ├─ DataToDatabase.java
    │  │  └─ RetrieveDataFromDatabase.java
    │  ├─ ⚙️ patterns
    │  │  ├─ COMMAND
    │  │  │  ├─ BaseCommandHandler.java
    │  │  │  ├─ Command.java
    │  │  │  ├─ JoinChannelCommand.java
    │  │  │  ├─ LeaveChannelCommand.java
    │  │  │  ├─ ListChannelCommand.java
    │  │  │  ├─ PrivateMessageCommand.java
    │  │  │  ├─ SendMexCommand.java
    │  │  │  └─ UserListCommand.java
    │  │  ├─ FACTORY-PATTERN
    │  │  │  ├─ Channel.java
    │  │  │  └─ ChannelFactory.java
    │  │  ├─ OBSERVER
    │  │  │  ├─ ConcreteChannel.java
    │  │  │  ├─ Observer.java
    │  │  │  └─ User.java
    │  │  ├─ SINGLETON
    │  │  │  └─ Server.java
    │  │  └─ STRATEGY
    │  │     ├─ Admin.java
    │  │     ├─ AdminActionStrategy.java
    │  │     ├─ BanAction.java
    │  │     ├─ KickAction.java
    │  │     ├─ PromoteAction.java
    │  │     └─ UnbanAction.java
    │  └─ 📲 GUI
    │     ├─ ChatClient.java
    │     └─ IRCChatApp.java
    ├─ .gitignore
    ├─ LICENSE
    └─ README.md
```

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- PATTERNS -->
## Pattern Used



<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- SYSTEM REQUIREMENTS -->
## System Requirements

> [!IMPORTANT]
> To correctly execute the behavior of this PWA you must have the following prerequisites pre-installed on your system:
> - Python 3.x
> - pip (Python Package Installer) - latest version

If you think you cannot meet these requirements, or at least not the recommended version, we recommend you check the installation of them by following this tutorial [here](https://github.com/r3aprz/Python-installation-tutorial).

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- SETTING UP VENV -->
## Setting Up the Virtual Environment

1. Ensure you have Python correctly installed (and at the latest version) on your system.

2. Open the terminal:
    - On Windows: Use Command Prompt.
    - On macOS and Linux: Use the system terminal.

3. Navigate to your project directory if necessary.

4. Create a virtual environment by running the following command:
    ```bash
    python -m venv venv
    ```
5. Then enter in the virtual environment
    - For MacOS or Linux systems:
        ```bash
        source venv/bin/activate
        ```
    - For Windows:
        ```bash
        venv\Scripts\activate
        ```
      > **Warning**  
      > Some versions of Windows may restrict the execution of scripts such as `.bat` from the terminal for security reasons. Make sure you have this option ***disabled***. You may find this option under the name *UAC* (User Access Control).
6. Install dependencies:
    ```bash
    pip install --upgrade -r requirements.txt
    ```
   Then, to update any modules:
    ```bash
    pip install --upgrade Flask Werkzeug click itsdangerous Jinja2 MarkupSafe Flask-WTF WTForms email_validator websocket_server
    ```

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- STARTING THE PWA -->
## Starting the PWA

1. Run the application by typing:
    ```bash
    python app.py
    ```
   The application should now be accessible at [http://127.0.0.1:5000/](http://127.0.0.1:5000/) in your web-browser.


<!-- LICENSE -->
## License
> [!WARNING]
> Distributed under the `MIT License`. See <a href="https://github.com/checcafor/IRC-p3proj/blob/main/LICENSE">LICENSE</a> for more information.
<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- CONTACT -->
## Contact
> [!NOTE]
> De Micco Francesco - [linkedin](https://www.linkedin.com/in/francesco-de-micco-b55034210/) - francesco.demicco001@studenti.uniparthenope.it <br>
> Formisano Francesca - [linkedin](https://www.linkedin.com/in/francesca-formisano-056460263/) - francesca.formisano001@studenti.uniparthenope.it <br>
>
> Project Link: [https://github.com/checcafor/IRC-p3proj.git](https://github.com/checcafor/IRC-p3proj.git)

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- MARKDOWN LINKS & IMAGES -->
[GitHub.com]: https://img.shields.io/badge/github-%23121011.svg?style=for-the-badge&logo=github&logoColor=white
