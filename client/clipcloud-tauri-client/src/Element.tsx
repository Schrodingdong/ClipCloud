import './Element.css'


interface ElementProps {
    content: string
}

const Element = (props: ElementProps) => {
    return <div className="clip-element">
        <div className="content">
            {props.content}
        </div>
        <div className="action">
            <button className="copy-button" >djsq</button>
        </div>
    </div>

}

export default Element;