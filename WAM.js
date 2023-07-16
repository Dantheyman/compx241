// ==UserScript==
// @name         WAM
// @author       Alma Walmsley, Daniel Jensen, Nathan Shakes
// @description  Edit any webpage with The World According To Me! Save and share your edits with the world!
// @version      0.13
// @match        *://*/*
// @run-at       document-body
// ==/UserScript==

(function() {
    var processes= 0;
    // initial functions
    var init = {
        // run at start
        init: function() {
            IO.init();
            Server.init();
            // load file from localStorage
            IO.load();
            // load CKEditor script
            CKEditor.loadScript();
            // observe changes to the document
            init.waitForText();
            //add the eddit button to the page
            UI.addUI();

            console.log("WAM is ready");
        },

        // waits for a text node to be added to the DOM before beginning processing.
        // This is to avoid traversing the body when it is not complete yet.
        waitForText: function() {
            var observer = new MutationObserver(function(mutationList) {
                // for each mutation
                for (var mutation of mutationList) {
                    for (var node of mutation.addedNodes) {
                        // process that node
                        if (node.nodeName == "#text") {
                            // found a text node
                            // end this function
                            observer.disconnect();
                            // start observing the document body
                            init.observe();
                            return;
                        }
                    }
                };
            });
            observer.observe(document, {
                childList: true,
                subtree: true,
            });
        },

        // observes changes to the document body
        observe: function() {
            // create a MutationObserver object
            const observer = new MutationObserver(function(mutationList) {
                // for each mutation
                mutationList.forEach((mutation) => {
                    // for each node in the mutation
                    mutation.addedNodes.forEach((node) => {
                        if (process.shouldProcess(node)) {
                            // process that node
                            process.newProcess(node);
                        }
                    });
                });
            });
            // process the document body
            process.newProcess(document.body);
            // set observer to process any further mutations to the document body
            observer.observe(document.body, {
                childList: true,
                subtree: true,
            });
        },
    }

    // functions for changing the UI
    var UI = {
        WAMDiv: null,
        isMin: false,

        //this function  adds a ui to the page that allows toggling between edit mode and surf mode
        //as well as sharing and retieving changes from a server
        addUI: function() {
            //create div to hold elements
            var div = document.createElement('div');
            // set global WAMDiv variable
            UI.WAMDiv = div;
            //Edit CSS of the div element
            div.style.position = "fixed";
            div.style.right = "30px";
            div.style.top = "0px";
            div.style.backgroundColor = "black";
            div.style.padding = "2px 3px";
            div.style.zIndex = 10000000;
            div.classList.add("noEdit");
            div.setAttribute("processed", "true");

            //if we want ui minimised then dont add these buttons
            if (!this.isMin){
                UI.addButton("EDIT PAGE", CKEditor.toggleEdit);
                UI.addButton("SHARE EDITS", Server.share);
                UI.addButton("LOAD EDITS", Server.load);
            }
            UI.addButton(this.isMin ? "+" : "-", UI.toggleHide);
            document.body.prepend(div);
        },

        // creates a button in the WAM UI
        // text: text of button
        // click: event on click
        addButton: function(text, click) {
            var button = document.createElement("button");
            button.innerHTML = text;
            button.style.all = "revert"; // set the button to default style
            button.style.margin = "2px";
            button.addEventListener("click", click);
            UI.WAMDiv.appendChild(button);
        },

        // toggles whether the UI is minimized/maximized
        toggleHide: function(){
            //set isMIN to opposite
            UI.isMin = !UI.isMin;
            //remove div
            UI.WAMDiv.remove();
            //add it back
            UI.addUI();
        },

    }


    // node processing functions
    var process = {
        // types of nodes to exclude
        exclude: ["#comment", "SCRIPT", "NOSCRIPT", "SVG", "STYLE"],

        // from https://stackoverflow.com/questions/2880957/detect-inline-block-type-of-a-dom-element
        getDisplayType: function(node) {
            // #text nodes are inline
            if (node.nodeType != "1") {
                return "inline";
            }
            else {
                // get whether element is inline or block
                var cStyle = node.currentStyle || window.getComputedStyle(node, "");
                return cStyle.display;
            }
        },

        // whether a node should be processed. returns false if a node is already processed
        shouldProcess: function(node) {
            return typeof node.getAttribute == 'function' && node.getAttribute("processed") != "true";
        },

        // returns true if a node has already been processed
        alreadyProcessed: function(node) {
            return typeof node.getAttribute == 'function' && node.getAttribute("processed") == "true";
        },

        // creates a new process of sibling elements
        newProcess: function(node) {
            return process.process(node, 0, false);
        },

        // processes a node
        // node: the node to process
        // previousInlineElementCount: the number of previous inline element siblings in a row
        // hasPreviousBlockSibling: true if the node has a previous element with display:block
        process: function(node, previousInlineElementCount, hasPreviousBlockSibling) {
            if (node.parentNode == null) {
                // return if no parent node
                return;
            }
            var skipNode = false;
            var isCK = false;
            var processed = process.alreadyProcessed(node);
            // dont add editability for CKEditor nodes
            if (node.classList != undefined && (node.classList.contains("ck-body-wrapper") || node.classList.contains("ck"))) {
                node.classList.add("noEdit");
                isCK = true;
            }
            // if node has text content
            var hasTextContent = (node.nodeName == "#text" || node.innerText != undefined) && node.textContent.replace(/\s/g, '').length != 0;
            // whether the parent indicates the children cant be edited
            var noChildEdit = node.parentNode.classList.contains("noEdit");
            if (noChildEdit) {
                // add no edit for its children
                if (node.classList != undefined) {
                    node.classList.add("noEdit");
                }
            }
            if (noChildEdit || !hasTextContent || processed || isCK) {
                // skip looking at this node's children
                skipNode = true;
            }
            // display type
            var display = process.getDisplayType(node);
            // if an element is an inline element
            var isInline = display == "inline" || display == "inline-block" || display == "none";
            // if next siblings are inline
            var siblingsAreInline = true;
            // if children are inline
            var childrenAreInline = true;

            var nextSibling = node.nextSibling;
            var previousSibling = node.previousSibling;

            if (!skipNode) {
                // process node's children
                if (node.childNodes.length != 0) {
                    childrenAreInline = process.newProcess(node.childNodes[0]);
                }
                // add editability to leaf container
                if (childrenAreInline && !isInline) {
                    process.createEditableNode(node);
                }
            }
            // isInline includes childrenAreInline
            isInline = (isInline && childrenAreInline) || (noChildEdit || !hasTextContent && node.nextSibling == null || isCK)
            if (node.nextSibling == null) {
                // end of siblings, we might need to create a container
                if (hasPreviousBlockSibling || !isInline) {
                    var currentNode = node;
                    if (isInline && !skipNode) {
                        // increase the number of inline elements
                        previousInlineElementCount ++;
                    }
                    else {
                        // set the current node to the previous sibling
                        currentNode = previousSibling;
                    }
                    // create a container from the current node going back previousInlineElementCount nodes
                    process.createContainer(currentNode, previousInlineElementCount);
                }
            }
            else {
                if (isInline) { // element is inline
                    if (!processed && (hasTextContent || previousInlineElementCount != 0)) {
                        // increase the number of inline elements
                        previousInlineElementCount ++;
                    }
                }
                else { // element is block
                    // create a container for the previousSibling
                    process.createContainer(previousSibling, previousInlineElementCount);
                    // reset previousInlineElementCount
                    previousInlineElementCount = 0;
                    // siblings have a block element
                    hasPreviousBlockSibling = true;
                }
            }
            // process the next sibling
            if (nextSibling != null) {
                siblingsAreInline = process.process(nextSibling, previousInlineElementCount, hasPreviousBlockSibling);
            }
            // mark node as processed
            if(typeof node.getAttribute === 'function') {
                node.setAttribute("processed", true);
            }
            // return true if the node is inline and the siblings are inline
            return isInline && siblingsAreInline;
        },


        // wraps the previous elementCount nodes from the currentNode in an outer container
        createContainer: function(currentNode, elementCount) {
            if (elementCount == 0) {
                return;
            }
            // decrease the number of inline elements and create a container from the previous sibling if the node is to be excluded or the node's text content is ""
            if (process.exclude.includes(currentNode.nodeName) || currentNode.textContent.replace(/\s/g, '').length == 0) {
                process.createContainer(currentNode.previousSibling, elementCount - 1);
                return;
            }
            // create editability for this node if the node only has one inline element
            if (elementCount == 1 && currentNode.nodeName != "#text") {
                process.createEditableNode(currentNode);
                return;
            }
            // element count > 1, group them in container
            // create div container element
            let container = document.createElement("div");
            // set processed to true
            container.setAttribute("processed", true);
            // insert the container after the current node
            currentNode.after(container);

            // loop elementCount times
            for (let i = 0; i < elementCount; i++) {
                // get the previous sibling
                let previousSibling = currentNode.previousSibling;
                // remove the current node from the DOM
                currentNode.remove();
                // insert the current node into the container as the first child
                container.prepend(currentNode);

                // set current node to the previous sibling
                currentNode = previousSibling;
            }
            // make the container an editable node
            process.createEditableNode(container);
        },

        // function for editable nodes
        createEditableNode: function(node) {
            // no children nodes are allowed to have editing capability
            node.classList.add("noEdit");
            // for debugging purposes, so we can see what nodes are editable
            node.classList.add("isEditable");
            // generate a hashcode for the node
            let hashCode = process.hashNode(node);
            // store the hashcode as an attibute of the node
            node.setAttribute("hashCode", hashCode);
            // store the hashcode and reference to the node in an object
            pageNodes[hashCode] = node;
            // check if node stored in file object
            let replacementText = IO.get(hashCode);
            if (replacementText != undefined) {
                // replace element outerHTML with the stored value in file
                node.innerHTML = replacementText;
            }
            // add editor function to this node
            CKEditor.createEditOnClick(node);
        },

        // generates a hash for a node, given a node object, using the node's innerHTML.
        // from https://stackoverflow.com/questions/7616461/generate-a-hash-from-string-in-javascript
        hashNode: function(node) {
            let str = node.innerHTML;
            return process.hash(str);
        },

        hash: function(str) {
            let hash = 0;
            for (let i = 0, len = str.length; i < len; i++) {
                let chr = str.charCodeAt(i);
                hash = (hash << 5) - hash + chr;
                hash |= 0; // Convert to 32bit integer
            }
            return hash;
        }
    }

    // CKEditor functions
    var CKEditor = {
        // url of CKEditor script
        url: "https://wam.interactwith.us/static/ckeditor.js",
        // current editor instance
        editorInstance: null,
        // current editor source element
        sourceElement: null,
        // whether we want to be in edit mode or not
        inEditMode: false,
        // if the editor text has been changed
        editorChanged: false,
        // hashcode of the element of the current editor instance
        editorInstanceHashcode: null,

        // adds script source to html
        loadScript: function() {
            // create script and set attributes
            var script = document.createElement('script');
            script.type = 'text/javascript';
            script.src = CKEditor.url;

            // add script to html
            document.head.appendChild(script);
        },

        // creates an event listener for a node to add CKEditor when it is clicked
        createEditOnClick: function(node) {
            // listen for pointerdown event, and call addEditForNode
            // removes event listener after clicked once
            node.addEventListener("pointerdown", CKEditor.addEditForNode, { once: true });
        },

        // destroys the current editor instance, if it exists
        destroyCurrent: function() {
            if (CKEditor.editorInstance != null)
            {
                if(CKEditor.editorChanged) {
                    // save the current editor data
                    CKEditor.saveEditorData();
                }
                // destroy CKEditor from the editorInstance
                CKEditor.editorInstance.destroy();
                // create onclick to add CKEditor for the source element
                CKEditor.createEditOnClick(CKEditor.sourceElement);
                // reset editorInstance and sourceElement to null
                CKEditor.editorInstance = null;
                CKEditor.sourceElement = null;
            }
        },

        // add edit for a node
        addEditForNode: function() {
            //if inEditMode is false then we want to return after adding onclick back
            if (!CKEditor.inEditMode){
                CKEditor.createEditOnClick(this);
                return;
            }
            // check InlineEditor has been created (if not, the script is probably still loading)
            if (typeof InlineEditor !== 'undefined') {
                // destroy the current editor
                CKEditor.destroyCurrent()
                // set the source element to this node
                CKEditor.sourceElement = this;
                CKEditor.sourceElement.innerHTML = CKEditor.sourceElement.innerHTML.trim();
                // create a new editor
                InlineEditor
                .create(this, {
                    updateSourceElementOnDestroy: true,
                })
                .then(editor => {
                    // set current editor instance to editor
                    CKEditor.editorInstance = editor;
                    CKEditor.editorInstanceHashcode = this.getAttribute("hashCode");
                    // run editor operations
                    CKEditor.editor();
                })
                .catch(error => {
                    // output any errors
                    console.error("CKEditor error: ", error);
                });
            } else {
                // setup new eventlistener for click
                CKEditor.createEditOnClick(this);
            }
        },

        // operations on the editor, saving to localStorage when edited
        editor: function() {
            CKEditor.editorChanged = false;
            // detect data changes to the editor
            CKEditor.editorInstance.model.document.on('change:data', () => {
                CKEditor.editorChanged = true;
            });

            // detect focus changes to the editor
            CKEditor.editorInstance.ui.focusTracker.on('change:isFocused', (evt, name, isFocused) => {
                // if editor is unfocused and some changes have been made
                if(!isFocused && CKEditor.editorChanged) {
                    // save the editor data
                    CKEditor.saveEditorData();
                }
            });
        },

        saveEditorData: function() {
            CKEditor.editorChanged = false;
            // save editor data to file at key hashCode
            IO.set(CKEditor.editorInstanceHashcode, CKEditor.sourceElement.innerHTML);
            // save file to localStorage
            IO.save();
        },

        //toggles between edit mode and surf mode
        toggleEdit: function() {
            //if in edting mode removes onclick function and removes an editor is one is intialized
            if (CKEditor.inEditMode){
                this.innerHTML = "EDIT PAGE"
                CKEditor.inEditMode=false;
                CKEditor.destroyCurrent();
            }
            //else changes to edit mode
            else{
                this.innerHTML ="STOP EDITING"
                CKEditor.inEditMode=true;
            }
        }
    }

    // functions for interacting with the file object and localStorage
    var IO = {
        // name of current URL
        currentLocation: null,
        // current location hash
        currentLocationHash: null,
        // name of key to store in localStorage
        fileKeyWord: null,
        // file object
        fileObj: {},

        init: function() {
            IO.currentLocation = location.origin + location.pathname.replace(/\/$/, "");
            IO.currentLocationHash = Math.abs(process.hash(IO.currentLocation));
        },

        // loads fileobj from localStorage
        load: function() {
            IO.fileKeyWord = "WAM_FILE_" + IO.currentLocationHash;
            var file = window.localStorage.getItem(IO.fileKeyWord);
            if (file != null) {
                IO.fileObj = JSON.parse(file);
            }
        },

        // saves fileobj to localStorage
        save: function() {
            window.localStorage.setItem(IO.fileKeyWord, JSON.stringify(IO.fileObj));
        },

        // returns the value given a key
        get: function(key) {
            return IO.fileObj[key];
        },

        // sets a key, value pair in the fileobj
        set: function(key, value) {
            IO.fileObj[key] = value;
        }
    }

    var pageNodes = {};

    //functions for interacting with our server
    var Server = {
        url: null,

        // initialize Server functions
        init: function() {
            // set the url to get and post to the server
            Server.url = "https://wam.interactwith.us/dynamic/" + IO.currentLocationHash + ".json";
        },

        // share to the server
        share: function() {
            fetch(Server.url, {
                method: "POST",
                body: JSON.stringify(IO.fileObj),
                headers: {
                    "Content-type": "application/json; charset=UTF-8",
                }
            })
            .then((response) => {
                if (response.status == 200) {
                    console.log("successfully saved");
                }
            });
        },

        // load from the server
        load: function() {
            fetch(Server.url)
            .then((response) => {
                if (response.status == 200) {
                    console.log("successfully loaded");
                    return response.json();
                }
                else {
                    return {};
                }
            }).then((data) => {
                // look through the response data
                Object.keys(data).forEach(key => {
                    // check if key in the DOM nodes hash values
                    if (key in pageNodes) {
                        // set the node innerHTML
                        pageNodes[key].innerHTML = data[key];
                        // store in the local JSON object
                        IO.set(key, data[key]);
                    }
                });
                // save to localStorage
                IO.save();
            });
        }
    }
    // initialize and run
    if (window.location == window.parent.location) {
        init.init();
    }
})();
