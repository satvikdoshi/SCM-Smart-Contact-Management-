localStorage.clear();
// add listener 
document.getElementById("theme_change_btn").addEventListener("click", (e) => {

    let currentTheme = getTheme();

    let newTheme = (currentTheme == 'dark' ? 'light' : 'dark')
    console.log(newTheme);

    document.querySelector('html').classList.add(newTheme);

    // remove the existing theme
    document.querySelector('html').classList.remove(currentTheme);


    //update the theme in localstorage 
    setTheme(newTheme)


    // change the text of button 
    document.getElementById("theme_change_btn").querySelector("span").textContent = newTheme == 'light' ? "Dark" : "Light"

})


// set theme to localstorage
function setTheme(theme) {
    localStorage.setItem("theme", theme)
}
// get theme from localstorage
function getTheme() {
    let theme = localStorage.getItem("theme")
    console.log(theme);

    if (theme == null) {
        return "light";
    }
    return theme;
}
// console.log("script loaded");

// add_contact.js 
document.querySelector("#image_file_input").addEventListener('change', (event) => {

    let file = event.target.files[0];
    let reader = new FileReader();
    reader.onload = function () {
        document.getElementById("upload_image_preview").src = reader.result;
    }
    reader.readAsDataURL(file);
})

// contacts.js 

var contactModal;
function openContactModal() {
    const viewContactModal = document.getElementById("view_contact_modal");

    const options = {
        placement: 'bottom-right',
        backdrop: 'dynamic',
        backdropClasses: 'bg-gray-900/50 dark:bg-gray-900/80 fixed inset-0 z-40',
        closable: true,

        onHide: () => {
            console.log('model is hidden');
        },
        onShow: () => {
            console.log('model is shown');
        },
        onToggle: () => {
            console.log('model is hidden');
        },
    };

    const instanceOptions = {
        id: 'view_contact_modal',
        override: true,
    };
    contactModal = new Modal(viewContactModal, options, instanceOptions)
    contactModal.show();
}

function closeContactModal() {
    contactModal.hide();
}

async function loadContactData(id) {

    console.log(id);
    const data = await (await fetch(`http://localhost:8080/api/contacts/${id}`)).json();
    console.log(data);

    // set the data on modal 
    document.querySelector("#contact_name").innerHTML = data.name;
    document.querySelector("#contact_email").innerHTML = data.email;

    document.querySelector("#contact_image").src = data.picture;
    document.querySelector("#contact_address").innerHTML = data.address;
    document.querySelector("#contact_phone").innerHTML = data.phone;
    document.querySelector("#contact_about").innerHTML = data.description;
    const contactFavorite = document.querySelector("#contact_favorite");
    if (data.favorite) {
        contactFavorite.innerHTML =
            "<i class='fas fa-star text-yellow-400'></i><i class='fas fa-star text-yellow-400'></i><i class='fas fa-star text-yellow-400'></i><i class='fas fa-star text-yellow-400'></i><i class='fas fa-star text-yellow-400'></i>";
    } else {
        contactFavorite.innerHTML = "Not Favorite Contact";
    }

    document.querySelector("#contact_github").innerHTML = data.githubLink;
    document.querySelector("#contact_linkdenInLInk").innerHTML = data.linkdenInLInk;

    openContactModal();
} 

    
// delete contact 
async function deleteContact(id) {
    Swal.fire({
      title: "Do you want to delete the contact?",
      icon: "warning",
      showCancelButton: true,
      confirmButtonText: "Delete",

    }).then((result) => {
      if (result.isConfirmed) {
        const url = "http://localhost:8080/user/contacts/delete/" + id;
        window.location.replace(url);
      }
    });
  }

// export data
function exportData(){

    TableToExcel.convert(document.getElementById("contact_table"), {
        name: "contacts.xlsx",
        sheet: {
          name: "Sheet 1"
        }
      });
}