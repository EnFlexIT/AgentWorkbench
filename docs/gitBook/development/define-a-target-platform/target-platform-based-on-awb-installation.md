---
description: Target Platform definition based on a local Agent.Workbench (AWB) installation
---

# ... based on AWB-Installation

To do so, open the Eclipse preferences \(menu _**Window**_ =&gt; _**Preferences**_\) and type the word 'target' into the search text field \(see image\).

![](../../.gitbook/assets/08_targetplatform_preferences.png)

Further, _**Add**_ a new Target Platform definition, choose **Nothing: Start with an empty target definition** on the first dialog page and press the _**Next**_ button underneath. In the subsequent dialog:

![](../../.gitbook/assets/09_targetplatform_addcontent.png)

1. Define the name of the Target Platform definition \(e.g. Agent.Workbench\)
2. Choose _**Add**_ to select the content \(bundles and / or features\) that define your target platform.
3. Select _**Installation**_, press _**Next**_ and _**Browse**_ to the installation of Agent.Workbench in your file system.
4. Again, click _**Next**_ to get a preview of the bundles / plugins to be used for your Target Platform or directly click _**Finish**_.

As a result, the field in the _**Locations**_ tab should point to your Agent.Workbench installation, saying that approx. 131 plugins were found. Click on _**Finish**_**.** The new target platform definition should now also be displayed in the list in the preference dialog. As final step mark this new definition as active \(_**tick the corresponding box**_\) and press _**Apply and Close**_.

Now you are prepared to develop your first agent project with Agent.Workbench.

