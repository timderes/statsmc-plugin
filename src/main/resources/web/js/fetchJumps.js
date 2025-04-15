
/**
 * Fetches the amount of jumps the players have made in the game.
 * This function is used for testing purposes.
 */
async function fetchJumps() {
    const response = await fetch('http://127.0.0.1:33333/mcstats/all_players?statistic=JUMP', {
        method: 'GET',
    });

    const data = await response.json();

    const statsDiv = document.getElementById('stats');
    if (data.error) {
        statsDiv.innerHTML = `<p>Error: ${data.error}</p>`;
    } else {
        statsDiv.innerHTML = '<h2>Player Statistics</h2>';
        const list = document.createElement('ul');
        for (const [player, stat] of Object.entries(data)) {
            const listItem = document.createElement('li');
            listItem.textContent = `${player}: ${stat}`;
            list.appendChild(listItem);
        }
        statsDiv.appendChild(list);
    }
}

fetchJumps()