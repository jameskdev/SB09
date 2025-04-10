const deleteButton = document.getElementById("delete-btn");
const createButton = document.getElementById("create-btn");
const editButton = document.getElementById("edit-btn");

if (deleteButton) {
    deleteButton.addEventListener("click", (ev) => { 
        let id = document.getElementById("content-id").value;
        fetch("/content/${id}", { method: "DELETE" }).then(y => { y.json().then(sd => { window.location.replace("/posts/all") }) });
    });
}

if (createButton) {
    createButton.addEventListener("click", (ev) => {
        const submitData = new FormData(document.getElementById("content_submit_form"));
        const request = JSON.stringify({
            subject : document.getElementById("subject").value,
            content : document.getElementById("content").value,
        });
        submitData.append("request", new Blob([request], { type: 'application/json' }));
        fetch("/content", { method: "POST", body: submitData }).then(x => { x.json().then(y => { window.location.replace("/posts/" + y.id) }) })
    });
}

if (editButton) {
    let params = new URLSearchParams(location.search);
    let cnt_id = params.get("id");
    editButton.addEventListener("click", (ev) => {
        const submitData = new FormData(document.getElementById("content_submit_form"));
        const request = JSON.stringify({
            subject : document.getElementById("subject").value,
            content : document.getElementById("content").value,
        });
        submitData.append("request", new Blob([request], { type: 'application/json' }));
        fetch("/content/" + cnt_id, { method: "PUT", body: submitData }).then(x => { x.json().then(y => { window.location.replace("/posts/" + y.id) }) })
    });
}