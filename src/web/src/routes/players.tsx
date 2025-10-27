import { createFileRoute } from "@tanstack/react-router";

export const Route = createFileRoute("/players")({
  component: PlayersPage,
  notFoundComponent: () => {
    return <>NOT FOUND!</>;
  },
});

function PlayersPage() {
  return <Players />;
}

function Players() {
  return <div className="App">PLAYERS</div>;
}

export default PlayersPage;
