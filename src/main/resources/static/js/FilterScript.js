
const apiUrl = 'http://localhost:8080/api/test';
async function SearchFetch(){
	
	fetch(apiUrl).then(response =>{
		if (!response.ok){
			throw new Error('Network response');	
		}
		return response.json();
	}).then(data =>{
		console.log('Data received:' , data);
	}).catch(error =>{
		console.error('Three has been a proble with your fetch operation',error)
	})

}