console.log("java script file is on")

const toggleSideBar = () =>{
	if($('.sidebar').is(":visible")){
		
		//true hide the sidebar
		
		$(".sidebar").css("display", "none");
		$(".content").css("margin-left", "0%");
		
	}else{
		
		//false show the sidebar
		
		$(".sidebar").css("display", "block");
		$(".content").css("margin-left", "20%");
	}
};

const search=()=>{
	console.log("searching......")
	let query= $("#search-input").val();

	if(query=='')
	{
		//hide serach box when you not enter any word
		$(".search-result").hide();
	}else
	{
		//search
		console.log(query)
		$(".search-result").show();

		//sending request to the server
		let url=`http://localhost:8282/search/${query}`;

		fetch(url).then((response) => {
			return response.json();
		}).then((data) => {
			//result data access......
			//console.log(data);

			let text=`<div class='list-group'>`;


			data.forEach((contact) => {

				text+=`<a href='/user/${contact.cId}/view-contact' class='list-group-item list-group-item-action'>${contact.name}</a>`

			});
			text+=`</div>`;

			$(".search-result").html(text); 
			$(".search-result").show(); 

		});

	}
}