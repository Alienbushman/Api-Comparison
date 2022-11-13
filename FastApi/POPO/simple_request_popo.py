from pydantic import BaseModel


class SimpleRequestPopo(BaseModel):
    id: int
    content: str
