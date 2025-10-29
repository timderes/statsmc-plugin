import Container from "react-bootstrap/Container";
import Spinner, { type SpinnerProps } from "react-bootstrap/Spinner";

type LoadingSpinnerProps = { text?: string } & SpinnerProps;

/**
 *
 */
const LoadingSpinner = ({ text, ...props }: LoadingSpinnerProps) => {
  return (
    <Container className="text-center vstack gap-4 justify-content-center align-items-center">
      <Spinner
        animation={props.animation || "border"}
        variant={props.variant || "primary"}
        {...props}
      />
      <p className="text-opacity-75 fw-semibold">{text || "Loading..."}</p>
    </Container>
  );
};

export default LoadingSpinner;
