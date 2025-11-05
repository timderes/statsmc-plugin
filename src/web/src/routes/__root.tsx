import { Link, Outlet, createRootRoute } from "@tanstack/react-router";
import { TanStackRouterDevtools } from "@tanstack/react-router-devtools";
import { Container, Nav, Navbar, Stack } from "react-bootstrap";
import ColorschemeToggle from "../components/navbar/ColorschemeToggle";

export const Route = createRootRoute({
  component: RootComponent,
});

function RootComponent() {
  return (
    <>
      <title>MC Stats</title>
      <Navbar expand="lg" className="bg-body-tertiary mb-4">
        <Container>
          <Navbar.Brand as={Link} to="/">
            MC Stats
          </Navbar.Brand>
          <Navbar.Toggle aria-controls="basic-navbar-nav" />
          <Navbar.Collapse id="basic-navbar-nav">
            <Nav className="me-auto w-100">
              <Nav.Link
                as={Link}
                activeProps={{
                  className: "fw-bold",
                }}
                activeOptions={{ exact: true }}
                to="/"
              >
                Dashboard
              </Nav.Link>
              <Nav.Link
                className="me-auto"
                as={Link}
                activeProps={{
                  className: "fw-bold",
                }}
                to="/players"
              >
                Players
              </Nav.Link>
              <ColorschemeToggle />
            </Nav>
          </Navbar.Collapse>
        </Container>
      </Navbar>
      <Container as={Stack} gap={4}>
        <Outlet />
      </Container>
      <TanStackRouterDevtools />
    </>
  );
}
