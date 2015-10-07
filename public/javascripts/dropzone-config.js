// "imageDropzone" is the camelized version of the HTML element's ID
Dropzone.options.imageDropzone = {
    paramName: "image", // The name that will be used to transfer the file
    maxFiles: 1, //  defines how many files this Dropzone handles
    maxFilesize: 5, // MB
    autoProcessQueue: false,　// Prevents Dropzone from uploading dropped files immediately
    thumbnailHeight: 240,
    thumbnailWidth: 240,    
    init: function() {
        var submitButton = document.querySelector("#upload-image")
            myDropzone = this; // closure

        submitButton.addEventListener("click", function() {
            myDropzone.processQueue(); // Tell Dropzone to process all queued files.
        });

        // You might want to show the submit button only when 
        // files are dropped here:
        this.on("addedfile", function() {
            // Show submit button here and/or inform user to click it.
        });
    }
};
