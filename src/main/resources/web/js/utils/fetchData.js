/**
 * Fetch data from a given URL and return the response as JSON.
 * 
 * @example fetchData('http://127.0.0.1:33333/api/').then(console.log).catch(console.error);
 * @param {string} url - The URL to fetch data from. 
 * @returns {Promise<any>} - A promise that resolves to the fetched data.
 * @throws {Error} - Throws an error if the fetch request fails or the response is not JSON.
 */
export const fetchData = async (url = '') => {
    if (!url) {
        throw new Error('URL is required to fetch data.');
    }

    try {
        const response = await fetch(url, {
            method: 'GET',
        });

        if (!response.ok) {
            throw new Error(`HTTP error! Status: ${response.status}`);
        }

        const data = await response.json();
        return data;
    } catch (error) {
        console.error('Error fetching data:', error);
        throw error;
    }
};
