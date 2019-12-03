// $(document).ready(function () {
$(function () {
    makeEditable({
            ajaxUrl: "ajax/admin/users/",
            datatableApi: $("#datatable").DataTable({
                "paging": false,
                "info": true,
                "columns": [
                    {
                        "data": "name"
                    },
                    {
                        "data": "email"
                    },
                    {
                        "data": "roles"
                    },
                    {
                        "data": "enabled"
                    },
                    {
                        "data": "registered"
                    },
                    {
                        "defaultContent": "Edit",
                        "orderable": false
                    },
                    {
                        "defaultContent": "Delete",
                        "orderable": false
                    }
                ],
                "order": [
                    [
                        0,
                        "asc"
                    ]
                ]
            }),
            updateTable: function () {
                           $.get("ajax/admin/users/", updateTableByData);
                        }
        }
    );
});

function updateCheckBox(id){
    $("#check").change(function () {
        if (this.checked == true){
            this.checked = false;
        } else
            this.checked = true;

        $.ajax({
            type: "PUT",
            url: "ajax/admin/users/",
            data: this.serialize() + id.serialize()
        }).done(function () {
            context.updateTable();
            successNoty("Updated");
        });
    });


}